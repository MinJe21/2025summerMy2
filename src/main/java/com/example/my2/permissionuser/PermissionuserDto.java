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
        boolean deleted;
        Long permissionId;
        Long userId;
        String username;

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
                    .permissionId(p.getId())
                    .deleted(p.isDeleted())
                    .permissionId(p.getPermission().getId())
                    .userId(p.getUser().getId())
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
}
