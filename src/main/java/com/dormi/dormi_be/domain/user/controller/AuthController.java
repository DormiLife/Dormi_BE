package com.dormi.dormi_be.domain.user.controller;

import com.dormi.dormi_be.domain.user.dto.request.LoginReqDto;
import com.dormi.dormi_be.domain.user.dto.request.ReissueReqDto;
import com.dormi.dormi_be.domain.user.dto.request.SignupReqDto;
import com.dormi.dormi_be.domain.user.dto.response.AuthResDto;
import com.dormi.dormi_be.domain.user.service.AuthService;
import com.dormi.dormi_be.global.common.BaseResponse;
import com.dormi.dormi_be.global.security.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public BaseResponse<AuthResDto> signup(
            @Valid
            @RequestBody
            SignupReqDto request
    ) {
        return BaseResponse.onSuccess(authService.signup(request));
    }

    @PostMapping("/login")
    public BaseResponse<AuthResDto> login(
            @Valid
            @RequestBody
            LoginReqDto request
    ) {
        return BaseResponse.onSuccess(authService.login(request));
    }

    @PostMapping("/refresh")
    public BaseResponse<AuthResDto> reissue(
            @Valid
            @RequestBody
            ReissueReqDto request
    ) {
        return BaseResponse.onSuccess(authService.reissue(request));
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout(@AuthUser Long userId) {
        authService.logout(userId);
        return BaseResponse.onSuccess(null);
    }
}
