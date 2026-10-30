package com.dormi.dormi_be.domain.user.service;

import com.dormi.dormi_be.domain.user.dto.request.UpdateUserReqDto;
import com.dormi.dormi_be.domain.user.dto.response.UserResDto;
import com.dormi.dormi_be.domain.user.entity.AuthAccount;
import com.dormi.dormi_be.domain.user.entity.User;
import com.dormi.dormi_be.domain.user.enums.AuthProvider;
import com.dormi.dormi_be.domain.user.repository.AuthAccountRepository;
import com.dormi.dormi_be.domain.user.repository.UserRepository;
import com.dormi.dormi_be.global.exception.BusinessException;
import com.dormi.dormi_be.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final AuthAccountRepository authAccountRepository;

    @Transactional
    public UserResDto updateMe(Long userId, UpdateUserReqDto req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String nickname = req.nickname();
        String phoneNumber = null;
        if (req.phoneNumber() != null) {
            phoneNumber = req.phoneNumber().replace("-", "");
        }

        if (nickname != null && !nickname.equals(user.getNickname())
            && userRepository.existsByNickname(nickname)) {
            throw new BusinessException(ErrorCode.NICKNAME_DUPLICATED);
        }

        if (phoneNumber != null && !phoneNumber.equals(user.getPhoneNumber())
                && userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new BusinessException(ErrorCode.PHONE_NUMBER_DUPLICATED);
        }

        user.update(nickname, req.roomNumber(), phoneNumber);

        String email = findEmail(userId);

        return UserResDto.from(user, email);
    }

    public UserResDto getMe(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String email = findEmail(userId);

        return UserResDto.from(user, email);
    }


    private String findEmail(Long userId) {
        return authAccountRepository.findByUser_IdAndProvider(userId, AuthProvider.EMAIL)
                .map(AuthAccount::getProviderUserId)
                .orElse(null);
    }
}
