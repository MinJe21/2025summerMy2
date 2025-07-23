package com.example.my2.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

public class UserDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginReqDto {
        String username;
        String password;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResDto {
        String refreshToken;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReqDto {
        String username;
        String password;
        String name;
        String nickname;
        String phone;

        public User toEntity(){
            return User.of(getUsername(), getPassword(), getName(), getNickname(), getPhone());
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateResDto {
        Long id;
    }
}
