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
        // 1. username으로 User 조회
        User user = null;
        System.out.println("1");
        if (param.getUsername() != null) {
            user = userRepository.findByUsername(param.getUsername());
            if (user == null) {
                try {
                    Long id = Long.parseLong(param.getUsername());
                    user = userRepository.findById(id).orElse(null);
                } catch (NumberFormatException ignore) {}
            }
        }
        System.out.println("2");

        if (user == null && param.getUserId() != null) {
            user = userRepository.findById(param.getUserId()).orElse(null);
        }
        System.out.println("3");

        if (user == null) {
            throw new RuntimeException("해당 유저를 찾을 수 없습니다");
        }
        System.out.println("4");

        // 2. permissionId로 Permission 조회
        Permission permission = permissionRepository.findById(param.getPermissionId())
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        // 3. 기존 Permissionuser 중복 여부 확인
        Permissionuser permissionuser = permissionuserRepository
                .findByPermissionIdAndUserId(param.getPermissionId(), user.getId());

        if (permissionuser == null) {
            // 새로 생성
            permissionuser = Permissionuser.of(permission, user);
        } else {
            // 이미 있으면 삭제 처리 (또는 복원 로직 필요 시 추가)
            permissionuser.setDeleted(true);
        }

        // 4. 저장 및 응답 DTO 변환
        return permissionuserRepository.save(permissionuser).toCreateResDto();
    }


    public void update(PermissionuserDto.UpdateReqDto param, Long reqUserId) {
        //permittedService.isPermitted(reqUserId, target, 120);
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
        //permittedService.isPermitted(reqUserId, target, 200);
        Permissionuser p = permissionuserRepository.findByIdAndDeletedFalse(param.getPermissionId())
                .orElseThrow(() -> new RuntimeException("PermissionDetail not found"));
        return PermissionuserDto.DetailResDto.from(p);
    }

    public PermissionuserDto.DetailResDto detail(PermissionuserDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    public List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param, Long reqUserId) {
        //permittedService.isPermitted(reqUserId, "permission", 200);
        List<Permissionuser> list = permissionuserRepository.findAllByPermissionIdAndDeletedFalse(param.getPermissionId());
        return list.stream()
                .map(PermissionuserDto.DetailResDto::from)
                .toList();
    }

    public List<PermissionuserDto.DetailResDto> detailList(List<PermissionuserDto.DetailResDto> list, Long reqUserId){
        List<PermissionuserDto.DetailResDto> newList = new ArrayList<>();
        for(PermissionuserDto.DetailResDto each : list){
            newList.add(get(PermissionuserDto.DetailReqDto.builder().permissionId(each.getPermissionId()).build(), reqUserId));
        }
        return newList;
    }

    public void deleteList(PermissionuserDto.DeleteListReqDto param, Long reqUserId) {
        for(Long id : param.getIds()){
            delete(PermissionuserDto.DeleteReqDto.builder().id(id).build(), reqUserId);
        }
    }
}
