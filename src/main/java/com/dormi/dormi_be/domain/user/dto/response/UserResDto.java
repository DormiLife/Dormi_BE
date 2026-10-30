package com.dormi.dormi_be.domain.user.dto.response;

import com.dormi.dormi_be.domain.user.entity.User;
import com.dormi.dormi_be.domain.user.enums.Gender;
import lombok.Builder;

@Builder
public record UserResDto (
        String name,
        Integer roomNumber,
        String email,
        String nickname,
        Gender gender,
        String phoneNumber
){
    public static UserResDto from(User user, String email) {
        return UserResDto.builder()
                .name(user.getName())
                .roomNumber(user.getRoomNumber())
                .email(email)
                .nickname(user.getNickname())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
