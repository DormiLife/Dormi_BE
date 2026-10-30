package com.dormi.dormi_be.domain.user.service;

import com.dormi.dormi_be.domain.user.dto.request.LoginReqDto;
import com.dormi.dormi_be.domain.user.dto.request.ReissueReqDto;
import com.dormi.dormi_be.domain.user.dto.request.SignupReqDto;
import com.dormi.dormi_be.domain.user.dto.response.AuthResDto;
import com.dormi.dormi_be.domain.user.entity.AuthAccount;
import com.dormi.dormi_be.domain.user.entity.RefreshToken;
import com.dormi.dormi_be.domain.user.entity.User;
import com.dormi.dormi_be.domain.user.enums.AuthProvider;
import com.dormi.dormi_be.domain.user.enums.UserStatus;
import com.dormi.dormi_be.domain.user.repository.AuthAccountRepository;
import com.dormi.dormi_be.domain.user.repository.RefreshTokenRepository;
import com.dormi.dormi_be.domain.user.repository.UserRepository;
import com.dormi.dormi_be.global.exception.BusinessException;
import com.dormi.dormi_be.global.exception.ErrorCode;
import com.dormi.dormi_be.global.security.JwtTokenProvider;
import com.dormi.dormi_be.global.security.TokenType;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResDto signup(SignupReqDto request) {
        String phoneNumber = request.phoneNumber().replace("-", "");
        String email = normalizeEmail(request.email());

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_DUPLICATED);
        }
        if (authAccountRepository.existsByProviderAndProviderUserId(AuthProvider.EMAIL, email)) {
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATED);
        }
        if (userRepository.existsByNickname(request.nickname())) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATED);
        }

        User user = userRepository.save(User.builder()
                .name(request.name())
                .roomNumber(request.roomNumber())
                .nickname(request.nickname())
                .gender(request.gender())
                .phoneNumber(phoneNumber)
                .build());

        authAccountRepository.save(AuthAccount.builder()
                .user(user)
                .provider(AuthProvider.EMAIL)
                .providerUserId(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .build());

        return issueTokens(user.getId());
    }

    @Transactional
    public AuthResDto login(LoginReqDto request) {
        AuthAccount account = authAccountRepository
                .findByProviderAndProviderUserId(AuthProvider.EMAIL, normalizeEmail(request.email()))
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), account.getPasswordHash())
                || account.getUser().getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        return issueTokens(account.getUser().getId());
    }

    @Transactional
    public AuthResDto reissue(ReissueReqDto request) {
        Long userId;
        try {
            userId = jwtTokenProvider.parseUserId(request.refreshToken(), TokenType.REFRESH);
        } catch (JwtException | IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        RefreshToken stored = refreshTokenRepository.findByUserId(userId)
                .filter(token -> token.getToken().equals(request.refreshToken()))
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN));

        return issueTokens(stored.getUserId());
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.findByUserId(userId).ifPresent(refreshTokenRepository::delete);
    }

    private AuthResDto issueTokens(Long userId) {
        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);
        LocalDateTime expiresAt = LocalDateTime.now()
                .plusNanos(jwtTokenProvider.getRefreshTokenExpirationMs() * 1_000_000L);

        refreshTokenRepository.findByUserId(userId).ifPresentOrElse(
                existing -> existing.rotate(refreshToken, expiresAt),
                () -> refreshTokenRepository.save(RefreshToken.builder()
                        .userId(userId)
                        .token(refreshToken)
                        .expiresAt(expiresAt)
                        .build())
        );

        return AuthResDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
