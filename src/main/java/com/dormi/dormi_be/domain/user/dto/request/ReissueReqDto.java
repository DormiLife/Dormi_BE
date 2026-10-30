package com.dormi.dormi_be.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ReissueReqDto (
    @NotBlank(message = "리프레시 토큰을 입력해주세요.")
    String refreshToken
) {

}
