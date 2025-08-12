package com.example.my2.permission;

import com.example.my2.permissiondetail.Permissiondetail;
import com.example.my2.permissiondetail.PermissiondetailDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

public class PermissionDto {

    public static String[][] targets = {
            {"permission","권한설정"}
            , {"user","사용자"}
            , {"notice", "공지사항"}
            , {"faq", "FAQ"}
    };

    @Getter @Setter @SuperBuilder
    @NoArgsConstructor @AllArgsConstructor
    public static class PermittedReqDto {
        Long userId;
        String target;
        Integer func;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateReqDto {
        private String title;
        private String content;


        public Permission toEntity(){
            return Permission.of(getTitle(),getContent());
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

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteListReqDto {
        private List<Long> ids;
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DetailReqDto {
        private Long id;
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

        String title;
        String content;

        String[][] targets; // 타겟 이름 가져가기!
        List<PermissiondetailDto.DetailResDto> details; // 이 권한이 가진 모든 디테일 가져가기!

        public static PermissionDto.DetailResDto from(Permission p) {
            return DetailResDto.builder()
                    .id(p.getId())
                    .deleted(p.isDeleted())
                    .createdAt(p.getCreatedAt())
                    .modifiedAt(p.getModifiedAt())
                    .title(p.getTitle())
                    .content(p.getContent())
                    .build();
        }
    }
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListReqDto {
        Long id;
        boolean deleted;
        LocalDateTime createdAt;
        LocalDateTime modifiedAt;
        String title;
        String content;
        String sdate;
        String fdate;
    }

    @Getter @Setter @SuperBuilder @NoArgsConstructor @AllArgsConstructor
    public static class PagedListReqDto {
        Boolean deleted;
        LocalDateTime sdate;
        LocalDateTime fdate;

        Integer offset;
        Integer callpage;
        Integer perpage;
        String orderway;
        String orderby;

        String title;

        public PermissionDto.PagedListResDto init(int listsize){
            Integer perpage = getPerpage(); //한번에 볼 글 갯수
            if(perpage == null || perpage < 1){
                perpage = 10;
            }
            if(perpage > 100){
                perpage = 100;
            }
            setPerpage(perpage);

            int totalpage = listsize / perpage; // 101 / 10 = >10 ?? 11이 되어야 하는데?
            if(listsize % perpage > 0){
                totalpage++;
            }

            Integer callpage = getCallpage();
            if(callpage == null || callpage < 1){
                //페이지 수가 없거나, 1보다 작은 페이수를 호출할 때
                callpage = 1;
            }
            if(callpage > totalpage){
                //전체 페이지보다 더 다음 페이지를 호출할때!
                callpage = totalpage;
            }
            if(callpage < 1){
                callpage = 1;
            }
            setCallpage(callpage);

            String orderby = getOrderby();
            if(orderby == null || orderby.isEmpty()){
                orderby = "id";
            }
            setOrderby(orderby);

            String orderway = getOrderway();
            if(orderway == null || orderway.isEmpty()){
                orderway = "DESC";
            }
            setOrderway(orderway);

            int offset = (callpage - 1) * perpage;
            System.out.println("offset : " + offset);
            setOffset(offset);

            return PermissionDto.PagedListResDto.builder()
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
}
