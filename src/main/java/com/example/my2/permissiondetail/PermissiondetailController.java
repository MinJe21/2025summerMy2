package com.example.my2.permissiondetail;

import com.example.my2.permission.PermissionDto;
import com.example.my2.permission.PermissionService;
import com.example.my2.permitted.PermittedService;
import com.example.my2.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/permissiondetail")
@RestController
@RequiredArgsConstructor
public class PermissiondetailController {
    private final PermissiondetailService permissiondetailService;
    private final PermittedService permittedService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/toggle")
    public ResponseEntity<PermissiondetailDto.CreateResDto> toggle(@RequestBody PermissiondetailDto.ToggleReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails
    ){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(permissiondetailService.toggle(params, reqUserId));
    }

    public Long getReqUserId(PrincipalDetails principalDetails){
        if(principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null){
            return null;
        }
        return principalDetails.getUser().getId();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<PermissiondetailDto.CreateResDto> create(@RequestBody PermissiondetailDto.CreateReqDto param,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        PermissiondetailDto.CreateResDto result = permissiondetailService.create(param, reqUserId);
        return ResponseEntity.ok(result);

    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public void update(@RequestBody PermissiondetailDto.UpdateReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        permissiondetailService.update(param, reqUserId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public void delete(@RequestBody PermissiondetailDto.DeleteReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        permissiondetailService.delete(param, reqUserId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<PermissiondetailDto.DetailResDto> detail(PermissiondetailDto.DetailReqDto params,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(permissiondetailService.detail(params, reqUserId));
    }
}
