package com.example.my2.permissiondetail;

import com.example.my2.permission.Permission;
import com.example.my2.permission.PermissionDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Permissiondetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    boolean deleted;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    LocalDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    LocalDateTime modifiedAt;

    @PrePersist
    public void onPrePersist() {
        this.deleted = false;
    }

    String target; // 어떤 테이블에 해당하는지! user notice
    Integer func; // 어떤 기능을 할지 create 110 read 200 update 120

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    public static Permissiondetail of(Long permissionId, String target, Integer func) {
        return Permissiondetail.builder()
                .deleted(false)
                .permission(Permission.builder().id(permissionId).build())
                .target(target)
                .func(func)
                .build();
    }

    public PermissiondetailDto.CreateResDto toCreateResDto() {
        return PermissiondetailDto.CreateResDto.builder().id(getId()).build();
    }
}
