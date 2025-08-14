package com.example.my2.user;

import com.example.my2.permissionuser.PermissionuserDto;
import com.example.my2.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public Long getReqUserId(PrincipalDetails principalDetails) {
        if (principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null) {
            return null;
        }
        return principalDetails.getUser().getId();
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/signup")
    public ResponseEntity<UserDto.CreateResDto> signup(@RequestBody UserDto.CreateReqDto param) {
        return ResponseEntity.ok(userService.signup(param, null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<UserDto.CreateResDto> create(@RequestBody UserDto.CreateReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(userService.create(params, reqUserId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<UserDto.DetailResDto> detail(UserDto.DetailReqDto param,
                                                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(userService.get(param, reqUserId));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("")
    public void update(@RequestBody UserDto.UpdateReqDto param,
                                                       @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        userService.update(param, reqUserId);
    }


    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("")
    public void delete(@RequestBody UserDto.DeleteReqDto param,
                       @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        userService.delete(param, reqUserId);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/list")
    public ResponseEntity<Void> deleteList(@RequestBody UserDto.DeleteListReqDto params,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        userService.deleteList(params, reqUserId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PreAuthorize(("hasRole('USER')"))
    @GetMapping("/pagedList")
    public ResponseEntity<UserDto.PagedListResDto> pagedList(UserDto.PagedListReqDto param,
                                                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(userService.pagedList(param, reqUserId));
    }
}
