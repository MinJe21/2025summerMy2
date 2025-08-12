package com.example.my2.permission;

import com.example.my2.permissiondetail.Permissiondetail;
import com.example.my2.permissionuser.Permissionuser;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Setter(AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Permission {
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

    private String title;
    private String content;

    @OneToMany(mappedBy = "permission")
    private List<Permissiondetail> permissiondetails;

    @OneToMany(mappedBy = "permission")
    private List<Permissionuser> permissionusers;

    public static Permission of(String title, String content) {
        return Permission.builder()
                .deleted(false)
                .title(title)
                .content(content)
                .build();
    }

    public PermissionDto.CreateResDto createResDto(){
        return PermissionDto.CreateResDto.builder()
                .id(getId())
                .build();
    }
}
