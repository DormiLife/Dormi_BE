package com.dormi.dormi_be.domain.user.dto.response;

import lombok.Builder;

@Builder
public record AuthResDto (
    String accessToken,
    String refreshToken
) {

}

