package com.example.my2.permissionuser;

import com.example.my2.permission.PermissionDto;
import com.example.my2.permission.PermissionService;
import com.example.my2.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/permissionuser")
@RestController
@RequiredArgsConstructor
public class PermissionuserController {
    private final PermissionuserService permissionuserService;

    public Long getReqUserId(PrincipalDetails principalDetails){
        if(principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null){
            return null;
        }
        return principalDetails.getUser().getId();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<PermissionuserDto.CreateResDto> create(@RequestBody PermissionuserDto.CreateReqDto param,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(permissionuserService.create(param, reqUserId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public void update(@RequestBody PermissionuserDto.UpdateReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        permissionuserService.update(param, reqUserId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public void delete(@RequestBody PermissionuserDto.DeleteReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        permissionuserService.delete(param, reqUserId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<PermissionuserDto.DetailResDto> detail(PermissionuserDto.DetailReqDto params,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(permissionuserService.detail(params, reqUserId));
    }
}
