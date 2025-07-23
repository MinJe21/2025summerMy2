package com.example.my2.permission;

import com.example.my2.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/permission")
@RestController
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    public Long getReqUserId(PrincipalDetails principalDetails){
        if(principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null){
            return null;
        }
        return principalDetails.getUser().getId();
    }

    @PostMapping("")
    public ResponseEntity<PermissionDto.CreateResDto> create(@RequestBody PermissionDto.CreateReqDto param,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(permissionService.create(param, reqUserId));
    }

    @PutMapping("")
    public void update(@RequestBody PermissionDto.UpdateReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        permissionService.update(param, reqUserId);
    }

    @DeleteMapping("")
    public void delete(@RequestBody PermissionDto.DeleteReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        permissionService.delete(param, reqUserId);
    }
}
