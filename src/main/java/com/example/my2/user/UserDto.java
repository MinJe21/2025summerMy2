package com.example.my2.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

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

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailResDto {
        Long id;
        boolean deleted;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;

        String username;
        String name;
        String nick;
        String phone;

        List<UserDto.DetailResDto> details; // 이 권한이 가진 모든 디테일 가져가기!

        public static UserDto.DetailResDto from(User p) {
            return DetailResDto.builder()
                    .id(p.getId())
                    .deleted(p.isDeleted())
                    .createdAt(p.getCreatedAt())
                    .modifiedAt(p.getModifiedAt())
                    .username(p.getUsername())
                    .name(p.getName())
                    .nick(p.getNickname())
                    .phone(p.getPhone())
                    .build();
        }
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailReqDto {
        private Long id;
    }

    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PagedListReqDto {

        private Boolean deleted;

        // 날짜 필터는 LocalDateTime + DateTimeFormat으로 안전하게 변환
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime sdate;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime fdate;

        private Integer offset;
        private Integer callpage;
        private Integer perpage;
        private String orderway;
        private String orderby;

        private String name; // 검색할 유저 이름

        public UserDto.PagedListResDto init(int listsize) {
            Integer perpage = getPerpage();
            if (perpage == null || perpage < 1) perpage = 10;
            if (perpage > 100) perpage = 100;
            setPerpage(perpage);

            int totalpage = listsize / perpage;
            if (listsize % perpage > 0) totalpage++;

            Integer callpage = getCallpage();
            if (callpage == null || callpage < 1) callpage = 1;
            if (callpage > totalpage) callpage = totalpage;
            if (callpage < 1) callpage = 1;
            setCallpage(callpage);

            if (orderby == null || orderby.isEmpty()) orderby = "id";
            setOrderby(orderby);

            if (orderway == null || orderway.isEmpty()) orderway = "DESC";
            setOrderway(orderway);

            int offset = (callpage - 1) * perpage;
            setOffset(offset);

            return UserDto.PagedListResDto.builder()
                    .totalpage(totalpage)
                    .callpage(getCallpage())
                    .perpage(getPerpage())
                    .listsize(listsize)
                    .build();
        }
    }


    @Getter @Setter @SuperBuilder
    @NoArgsConstructor @AllArgsConstructor
    public static class PagedListResDto {
        Integer listsize;
        Integer totalpage;
        Integer callpage;
        Integer perpage;
        Object list;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateReqDto {
        private Long id;
        private boolean deleted;
        private String username;
        private String name;
        private String nickname;
        private String phone;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateResDto {
        private Long id;
        private boolean deleted;
        private String username;
        private String name;
        private String nickname;
        private String phone;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteReqDto {
        private Long id;
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
