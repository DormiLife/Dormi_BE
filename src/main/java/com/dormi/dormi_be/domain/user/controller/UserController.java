package com.dormi.dormi_be.domain.user.controller;

import com.dormi.dormi_be.domain.user.dto.request.UpdateUserReqDto;
import com.dormi.dormi_be.domain.user.dto.response.UserResDto;
import com.dormi.dormi_be.domain.user.service.UserService;
import com.dormi.dormi_be.global.common.BaseResponse;
import com.dormi.dormi_be.global.security.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/me")
    public BaseResponse<UserResDto> updateMe(
            @AuthUser
            Long userId,
            @Valid
            @RequestBody
            UpdateUserReqDto request
    ) {
        return BaseResponse.onSuccess(userService.updateMe(userId, request));
    }

    @GetMapping("/me")
    public BaseResponse<UserResDto> getMe(
            @AuthUser
            Long userId
    ) {
        return BaseResponse.onSuccess(userService.getMe(userId));
    }
}
