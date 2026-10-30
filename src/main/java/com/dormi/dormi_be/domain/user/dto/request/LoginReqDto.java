package com.dormi.dormi_be.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginReqDto (
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password
) {

}
