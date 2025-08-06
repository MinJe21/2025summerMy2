package com.example.my2.permissiondetail;


import com.example.my2.permitted.PermittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissiondetailService {
    private final String target = "permission";

    private final PermissiondetailRepository permissiondetailRepository;
    private final PermittedService permittedService;

    public PermissiondetailDto.CreateResDto toggle(PermissiondetailDto.ToggleReqDto param, Long reqUserId) {
        Permissiondetail permissiondetail = permissiondetailRepository.findByPermissionIdAndTargetAndFunc(param.getPermissionId(), param.getTarget(), param.getFunc());
        if(permissiondetail == null) {
            //없는데 생성하라고 하네!
            if(param.getAlive()){
                return create(PermissiondetailDto.CreateReqDto.builder()
                        .permissionId(param.getPermissionId())
                        .target(param.getTarget())
                        .func(param.getFunc())
                        .build(), reqUserId);
            }
        } else {
            permittedService.isPermitted(reqUserId, target, 120);
            //있는데 바꿔주기!
            permissiondetail.setDeleted(!param.getAlive());
            return permissiondetailRepository.save(permissiondetail).toCreateResDto();
        }
        return PermissiondetailDto.CreateResDto.builder().id((long) -100).build();
    }

    /**/
    public PermissiondetailDto.CreateResDto create(PermissiondetailDto.CreateReqDto param, Long reqUserId) {
        PermissiondetailDto.CreateResDto res = permissiondetailRepository.save(param.toEntity()).toCreateResDto();
        return res;
    }

    public void update(PermissiondetailDto.UpdateReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 120);
        Permissiondetail permissiondetail = permissiondetailRepository.findById(param.getPermissionId()).orElse(null);
        if(permissiondetail == null){
            throw new RuntimeException("no data");
        }
        if(param.getDeleted() != null) {
            param.setDeleted(param.getDeleted());
        }
        if(param.getTarget() != null) {
            param.setTarget(param.getTarget());
        }
        if(param.getFunc() != null) {
            param.setFunc(param.getFunc());
        }
        permissiondetailRepository.save(permissiondetail);
    }

    public void delete(PermissiondetailDto.DeleteReqDto param, Long reqUserId) {
        update(PermissiondetailDto.UpdateReqDto.builder().permissionId(param.getPermissionId()).deleted(true).build(), reqUserId);
    }


    public PermissiondetailDto.DetailResDto get(PermissiondetailDto.DetailReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, target, 200);
        Permissiondetail p = permissiondetailRepository.findByIdAndDeletedFalse(param.getPermissionId())
                .orElseThrow(() -> new RuntimeException("PermissionDetail not found"));
        return PermissiondetailDto.DetailResDto.from(p);
    }

    public PermissiondetailDto.DetailResDto detail(PermissiondetailDto.DetailReqDto param, Long reqUserId) {
        return get(param, reqUserId);
    }

    public List<PermissiondetailDto.DetailResDto> list(PermissiondetailDto.ListReqDto param, Long reqUserId) {
        permittedService.isPermitted(reqUserId, "permission", 200);

        List<Permissiondetail> list = permissiondetailRepository.findAllByPermissionIdAndDeletedFalse(param.getPermissionId());

        return list.stream()
                .map(PermissiondetailDto.DetailResDto::from)
                .collect(Collectors.toList());
    }

    public List<PermissiondetailDto.DetailResDto> detailList(List<PermissiondetailDto.DetailResDto> list, Long reqUserId){
        List<PermissiondetailDto.DetailResDto> newList = new ArrayList<>();
        for(PermissiondetailDto.DetailResDto each : list){
            newList.add(get(PermissiondetailDto.DetailReqDto.builder().permissionId(each.getPermissionId()).build(), reqUserId));
        }
        return newList;
    }



}
