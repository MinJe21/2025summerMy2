package com.example.my2.permitted;

import com.example.my2.Exception.NoPermissionException;
import com.example.my2.permission.PermissionDto;
import com.example.my2.permission.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class PermittedService {
    private final PermissionRepository permissionRepository;

    public void isPermitted(Long userId, String target, int func) {
        // -200인 경우에는, 그냥 무사 통과를 부탁한것!!
        if(userId != -200){
            if(!permitted(PermissionDto.PermittedReqDto.builder().userId(userId).target(target).func(func).build())){
                throw new NoPermissionException("no auth");
            }
        }
    }

    public boolean permitted(PermissionDto.PermittedReqDto param) {
        long count = permissionRepository.countPermissionByTargetAndFuncAndUser(
                param.getTarget(),
                param.getFunc(),
                param.getUserId()
        );
        return count > 0;
    }
}

