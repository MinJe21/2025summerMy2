package com.example.my2.permissionuser;

import com.example.my2.permission.Permission;
import com.example.my2.permission.PermissionDto;
import com.example.my2.permissiondetail.Permissiondetail;
import com.example.my2.permissiondetail.PermissiondetailDto;
import com.example.my2.user.User;
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
public class Permissionuser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean deleted;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @PrePersist
    public void onPrePersist() {
        this.deleted = false;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 팩토리 메서드 예시
    public static Permissionuser of(Permission permission, User user) {
        return Permissionuser.builder()
                .deleted(false)
                .permission(permission)
                .user(user)
                .build();
    }

    public PermissionuserDto.CreateResDto toCreateResDto() {
        return PermissionuserDto.CreateResDto.builder()
                .id(this.id)
                .build();
    }
}

