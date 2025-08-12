package com.example.my2.user;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public UserDto.PagedListResDto pagedList(UserDto.PagedListReqDto param, Long userId){
         // 전체 개수 먼저 조회
        long totalCount = userRepository.countBySearchConditions(
                param.getDeleted(),
                param.getName(),
                param.getSdate(),
                param.getFdate()
        );

        // 🧠 기존 방식처럼 param.init() 호출
        UserDto.PagedListResDto res = param.init((int) totalCount); // init이 offset 등 세팅

        // 정렬 + 페이징 처리된 결과 가져오기
        Pageable pageable = PageRequest.of(param.getCallpage() - 1, param.getPerpage(), Sort.by(
                param.getOrderway().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                param.getOrderby()
        ));

        Page<User> page = userRepository.findBySearchConditions(
                param.getDeleted(),
                param.getName(),
                param.getSdate(),
                param.getFdate(),
                pageable
        );

        res.setList(
                page.getContent().stream()
                        .map(UserDto.DetailResDto::from)
                        .toList()
        );

        return res;
    }
}
