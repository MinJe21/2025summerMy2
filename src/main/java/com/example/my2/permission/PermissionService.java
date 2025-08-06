package com.example.my2.permission;

import com.amazonaws.services.glue.model.EntityNotFoundException;
import com.example.my2.permissiondetail.Permissiondetail;
import com.example.my2.permissiondetail.PermissiondetailDto;
import com.example.my2.permissiondetail.PermissiondetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissiondetailService permissiondetailService;
    private final String target = "permission";

    public PermissionDto.CreateResDto create(PermissionDto.CreateReqDto param, Long reqUserId) {
        return permissionRepository.save(param.toEntity()).createResDto();
    }

    public void update(PermissionDto.UpdateReqDto param, Long reqUserId) {
        Permission p = permissionRepository.findById(param.getId()).orElse(null);
        if (p == null) {
            throw new RuntimeException("no permission found with id: " + param.getId());
        }

        if(param.getDeleted() != null) {
            param.setDeleted(param.getDeleted());
        }
        if(param.getTitle() != null) {
            param.setTitle(param.getTitle());
        }
        if(param.getContent() != null) {
            param.setContent(param.getContent());
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

}
