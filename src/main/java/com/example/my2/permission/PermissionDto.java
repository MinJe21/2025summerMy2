package com.example.my2.permission;

import lombok.*;

public class PermissionDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReqDto {
        private String title;
        private String content;

        public Permission toEntity(){
            return Permission.of(getTitle(), getContent());
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateResDto {
        private Long id;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateReqDto {
        private Long id;
        private Boolean deleted;
        private String title;
        private String content;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteReqDto {
        private Long id;
    }

}
