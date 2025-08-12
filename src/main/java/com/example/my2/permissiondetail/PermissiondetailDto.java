package com.example.my2.permissiondetail;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class PermissiondetailDto {

    @Getter @Setter @SuperBuilder
    @NoArgsConstructor @AllArgsConstructor
    public static class ToggleReqDto{
        Boolean alive; //true면 생성, false면 삭제!
        Long permissionId;
        String target;
        Integer func;
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReqDto {
        private Long permissionId;
        private String target;
        private Integer func;

        public Permissiondetail toEntity(){
            return Permissiondetail.of(getPermissionId(), getTarget(), getFunc());
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
        private Long permissionId;
        private Boolean deleted;
        private String target;
        private Integer func;
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

        String target;
        Integer func;

        String[][] targets; // 타겟 이름 가져가기!
        List<DetailResDto> details; // 이 권한이 가진 모든 디테일 가져가기!

        public static DetailResDto from(Permissiondetail p) {
            return DetailResDto.builder()
                    .permissionId(p.getId())
                    .deleted(p.isDeleted())
                    .createdAt(p.getCreatedAt())
                    .modifiedAt(p.getModifiedAt())
                    .target(p.getTarget())
                    .func(p.getFunc())
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
