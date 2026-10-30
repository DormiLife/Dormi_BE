package com.dormi.dormi_be.domain.user.dto.request;

import com.dormi.dormi_be.domain.user.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignupReqDto (
    @NotBlank(message = "이메일을 입력해주세요.")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    String email,

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9])[\\x21-\\x7E]{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 각각 1자 이상 포함해야 하며 공백이나 한글은 사용할 수 없습니다."
    )
    String password,

    @NotBlank(message = "이름을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{1,10}$", message = "이름은 한글 또는 영문 10자 이하로 입력해주세요.")
    String name,

    @NotNull(message = "방 번호를 입력해주세요.")
    @Min(value = 100, message = "방 번호는 0으로 시작하지 않는 3자리 숫자여야 합니다.")
    @Max(value = 999, message = "방 번호는 0으로 시작하지 않는 3자리 숫자여야 합니다.")
    Integer roomNumber,

    @NotBlank(message = "별명을 입력해주세요.")
    @Size(min = 2, max = 10, message = "별명은 2자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9_]*$", message = "별명은 한글, 영문, 숫자, 밑줄(_)만 사용할 수 있습니다.")
    String nickname,

    @NotNull(message = "성별을 선택해주세요.")
    Gender gender,

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^01[016789]-?\\d{3,4}-?\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.")
    String phoneNumber
){

}
