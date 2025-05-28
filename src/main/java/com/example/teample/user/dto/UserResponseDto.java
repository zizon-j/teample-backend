package com.example.teample.user.dto;


import com.example.teample.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserResponseDto {
    private Long userId;
    private String userEmail;
    private String userName;
    private LocalDate userBirthday;
    private String roleName;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .userBirthday(user.getUserBirthday())
                .roleName(user.getRole().getName())
                .build();
    }
}
