package com.example.my2.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

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
}
