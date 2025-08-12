package com.example.my2.permission;

import com.amazonaws.services.glue.model.EntityNotFoundException;
import com.example.my2.permissiondetail.PermissiondetailDto;
import com.example.my2.permissiondetail.PermissiondetailService;
import com.example.my2.permissionuser.PermissionuserDto;
import com.example.my2.permitted.PermittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissiondetailService permissiondetailService;
    private final PermittedService permittedService;
    private final String target = "permission";

    public PermissionDto.CreateResDto create(PermissionDto.CreateReqDto param, Long reqUserId) {
        //permittedService.isPermitted(reqUserId, target, 110);
        return permissionRepository.save(param.toEntity()).createResDto();
    }

    public void update(PermissionDto.UpdateReqDto param, Long reqUserId) {
        Permission p = permissionRepository.findById(param.getId())
                .orElseThrow(() -> new RuntimeException("no permission found with id: " + param.getId()));

        p.setDeleted(param.isDeleted());

        if (param.getTitle() != null) {
            p.setTitle(param.getTitle());
        }
        if (param.getContent() != null) {
            p.setContent(param.getContent());
        }
        permissionRepository.save(p);
    }


    public void delete(PermissionDto.DeleteReqDto param, Long reqUserId) {
        update(PermissionDto.UpdateReqDto.builder().id(param.getId()).deleted(true).build(), reqUserId);
    }

    public PermissionDto.DetailResDto get(PermissionDto.DetailReqDto param, Long reqUserId) {
        Permission p = permissionRepository.findByIdAndDeletedFalse(param.getId())
                .orElseThrow(() -> new EntityNotFoundException("Permission not found"));
        PermissionDto.DetailResDto res = PermissionDto.DetailResDto.builder()
                .id(p.getId())
                .deleted(p.isDeleted())
                .createdAt(p.getCreatedAt())
                .modifiedAt(p.getModifiedAt())
                .title(p.getTitle())
                .content(p.getContent())
                .build();

        res.setDetails(
                permissiondetailService.list(PermissiondetailDto.ListReqDto.builder().deleted(false).permissionId(res.getId()).build(), reqUserId)
        );
        //타겟 이름 리스트 관리!
        res.setTargets(PermissionDto.targets);
        return res;
    }

    public PermissionDto.DetailResDto detail(PermissionDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    public List<PermissionDto.DetailResDto> list(PermissionDto.ListReqDto param, Long reqUserId) {
        List<Permission> list = permissionRepository.findAllByIdAndDeletedFalse(param.getId());

        return list.stream()
                .map(PermissionDto.DetailResDto::from)
                .collect(Collectors.toList());
    }

    public List<PermissionDto.DetailResDto> detailList(List<PermissionDto.DetailResDto> list, Long reqUserId){
        List<PermissionDto.DetailResDto> newList = new ArrayList<>();
        for(PermissionDto.DetailResDto each : list){
            newList.add(get(PermissionDto.DetailReqDto.builder().id(each.getId()).build(), reqUserId));
        }
        return newList;
    }

    public PermissionDto.PagedListResDto pagedList(PermissionDto.PagedListReqDto param, Long reqUserId) {
        long totalCount = permissionRepository.countBySearchConditions(
                param.getDeleted(),
                param.getTitle(),
                param.getSdate(),
                param.getFdate()
        );

        PermissionDto.PagedListResDto res = param.init((int) totalCount);

        Pageable pageable = PageRequest.of(param.getCallpage() - 1, param.getPerpage(),
                Sort.by(param.getOrderway().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        param.getOrderby()));

        Page<Permission> page = permissionRepository.findBySearchConditions(
                param.getDeleted(),   // ← 이게 true면 삭제된 것만, false면 안 삭제된 것만
                param.getTitle(),
                param.getSdate(),
                param.getFdate(),
                pageable
        );

        res.setList(page.getContent().stream()
                .map(PermissionDto.DetailResDto::from)
                .toList());

        return res;
    }


    public void deleteList(PermissionDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()) {
            delete(PermissionDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }

}
