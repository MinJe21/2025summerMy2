package com.example.my2.user;

import com.example.my2.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    public Long getReqUserId(PrincipalDetails principalDetails){
        if(principalDetails == null || principalDetails.getUser() == null || principalDetails.getUser().getId() == null){
            return null;
        }
        return principalDetails.getUser().getId();
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto.CreateResDto> signup(@RequestBody UserDto.CreateReqDto param){
        return ResponseEntity.ok(userService.signup(param, null));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<UserDto.CreateResDto> create(@RequestBody UserDto.CreateReqDto params, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Long reqUserId = getReqUserId(principalDetails);
        return ResponseEntity.ok(userService.create(params, reqUserId));
    }
}
