package com.example.my2.user;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDto.CreateResDto signup(@RequestBody UserDto.CreateReqDto param, Long userId){
        return create(param, userId);
    }

    public UserDto.CreateResDto create(UserDto.CreateReqDto param, Long userId){
        User u = userRepository.findByUsername(param.getUsername());
        if(u != null){
            throw new RuntimeException("already exist");
        }
        param.setPassword(bCryptPasswordEncoder.encode(param.getPassword()));
        User newUser = userRepository.save(param.toEntity());
        return newUser.createResDto();
    }
}
