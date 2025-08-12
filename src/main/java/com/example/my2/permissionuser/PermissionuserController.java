package com.example.my2.permissionuser;

import com.example.my2.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/list")
    public List<PermissionuserDto.DetailResDto> list(PermissionuserDto.ListReqDto param,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        return permissionuserService.list(param, reqUserId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<PermissionuserDto.DetailResDto> detail(PermissionuserDto.DetailReqDto params,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(permissionuserService.detail(params, reqUserId));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/list")
    public ResponseEntity<Void> deleteList(@RequestBody PermissionuserDto.DeleteListReqDto params,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        permissionuserService.deleteList(params, reqUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
