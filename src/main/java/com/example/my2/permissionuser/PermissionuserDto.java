package com.example.my2.permissionuser;

import com.example.my2.permission.Permission;
import com.example.my2.permissiondetail.Permissiondetail;
import com.example.my2.permissiondetail.PermissiondetailDto;
import com.example.my2.user.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class PermissionuserDto {

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReqDto {
        private Long permissionId;
        private Long userId;
        private String username;

        public Permissionuser toEntity(Permission permission, User user) {
            return Permissionuser.of(permission, user);
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
        private boolean deleted;
        private Long permissionId;
        private Long userId;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteReqDto {
        private Long id;
        private Long permissionId;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailReqDto {
        private Long permissionId;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResDto {
        Long permissionId;
        boolean deleted;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;

        Long userId;

        String userUsername;

        public static PermissionuserDto.DetailResDto from(Permissionuser p) {
            return DetailResDto.builder()
                    .permissionId(p.getPermission().getId())
                    .deleted(p.isDeleted())
                    .createdAt(p.getCreatedAt())
                    .modifiedAt(p.getModifiedAt())
                    .userId(p.getUser().getId())
                    .userUsername(p.getUser().getUsername())
                    .build();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListReqDto {
        Long permissionId;
        boolean deleted;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteListReqDto {
        List<Long> ids;
    }
}
