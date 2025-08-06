package com.example.my2.permissionuser;


import com.example.my2.permission.Permission;
import com.example.my2.permission.PermissionRepository;
import com.example.my2.permissionuser.Permissionuser;
import com.example.my2.permissionuser.PermissionuserDto;
import com.example.my2.permissionuser.PermissionuserRepository;
import com.example.my2.permitted.PermittedService;
import com.example.my2.user.User;
import com.example.my2.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionuserService {

    private final String target = "permission";

    private final PermissionRepository permissionRepository;
    private final PermissionuserRepository permissionuserRepository;
    private final UserRepository userRepository;
    final PermittedService permittedService;


    public PermissionuserDto.CreateResDto create(PermissionuserDto.CreateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 110);

        User u = userRepository.findByUsername(param.getUsername());
        if(u == null) {
            u = userRepository.findById(Long.parseLong(param.getUsername())).orElse(null);
        }
        if(u == null) {
            throw new RuntimeException("User not found");
        }
        param.setUsername(u.getUsername());

        Permissionuser permissionuser = permissionuserRepository.findByPermissionIdAndUserId(param.getPermissionId(), param.getUserId());
        Permission permission = permissionRepository.findById(param.getPermissionId()).orElseThrow();
        User user = userRepository.findById(param.getUserId()).orElseThrow();
        if(permissionuser == null) {
            permissionuser = param.toEntity(permission, user);
        } else{
            permissionuser.setDeleted(true);
        }

        return permissionuserRepository.save(permissionuser).toCreateResDto();
    }

    public void update(PermissionuserDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Permissionuser permissionuser = permissionuserRepository.findById(param.getPermissionId()).orElse(null);
        if(permissionuser == null){
            throw new RuntimeException("no data");
        }
        if (param.isDeleted()) {
            permissionuser.setDeleted(param.isDeleted());
        }
        if (param.getUserId() != null) {
            User user = userRepository.findById(param.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            permissionuser.setUser(user);        }

        permissionuserRepository.save(permissionuser);
    }

    public void delete(PermissionuserDto.DeleteReqDto param, Long reqUserId) {
        update(PermissionuserDto.UpdateReqDto.builder().permissionId(param.getPermissionId()).deleted(true).build(), reqUserId);
    }


    public PermissionuserDto.DetailResDto get(PermissionuserDto.DetailReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 200);
        Permissionuser p = permissionuserRepository.findByIdAndDeletedFalse(param.getPermissionId())
                .orElseThrow(() -> new RuntimeException("PermissionDetail not found"));
        return PermissionuserDto.DetailResDto.from(p);
    }

    public PermissionuserDto.DetailResDto detail(PermissionuserDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    public List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, "permission", 200);

        List<Permissionuser> list = permissionuserRepository.findAllByPermissionIdAndDeletedFalse(param.getPermissionId());

        return list.stream()
                .map(PermissionuserDto.DetailResDto::from)
                .collect(Collectors.toList());
    }

    public List<PermissionuserDto.DetailResDto> detailList(List<PermissionuserDto.DetailResDto> list, Long reqUserId){
        List<PermissionuserDto.DetailResDto> newList = new ArrayList<>();
        for(PermissionuserDto.DetailResDto each : list){
            newList.add(get(PermissionuserDto.DetailReqDto.builder().permissionId(each.getPermissionId()).build(), reqUserId));
        }
        return newList;
    }
}
