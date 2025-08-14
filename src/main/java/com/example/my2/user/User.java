package com.example.my2.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private String username;
    private String password;
    private String name;
    private String nickname;
    private String phone;

    public static User of(String username, String password, String name, String nickname, String phone){
        return User.builder()
                .deleted(false)
                .username(username)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phone(phone)
                .build();
    }

    public UserDto.CreateResDto createResDto(){
        return UserDto.CreateResDto.builder()
                .id(getId())
                .build();
    }
}
