package com.dormi.dormi_be.domain.user.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserReqDto (
        @Size(min = 2, max = 10, message = "별명은 2자 이상 10자 이하여야 합니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9_]*$", message = "별명은 한글, 영문, 숫자, 밑줄(_)만 사용할 수 있습니다.")
        String nickname,

        @Min(value = 100, message = "방 번호는 0으로 시작하지 않는 3자리 숫자여야 합니다.")
        @Max(value = 999, message = "방 번호는 0으로 시작하지 않는 3자리 숫자여야 합니다.")
        Integer roomNumber,

        @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
        String phoneNumber
){
}
