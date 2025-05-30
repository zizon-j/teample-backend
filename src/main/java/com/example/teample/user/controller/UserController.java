package com.example.teample.user.controller;


import com.example.teample.user.domain.User;
import com.example.teample.user.dto.UserRequestDto;
import com.example.teample.user.dto.UserResponseDto;
import com.example.teample.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users") //기본 url 경로 지정
@RequiredArgsConstructor //final로 선언된 필드를 자동으로 생성자 주입
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) { //사용자 요청을 받은 후 DB에 저장하고 저장된 User반환
            return userService.create(requestDto);
    }

    @GetMapping("")
    public UserResponseDto getUserByEmail(HttpSession session) { //이메일로 유저 검색
        String email = (String) session.getAttribute("email");

        log.info("email: {}", email);
        if(email.isEmpty()) {throw new NullPointerException();}


        return UserResponseDto.from(userService.getByEmail(email));
    }

    @GetMapping("/list")
    public List<UserResponseDto> getUsers() { //모든 사용자 목록 조회해서 JSON으로 반환
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable long id) { //특정 ID를 가진 사용자 조회
        return userService.findById(id);
    }


    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable long id, @RequestBody UserRequestDto requestDto) {
        return userService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id) { //특정 사용자 삭제
        userService.delete(id);
        return ResponseEntity.ok("삭제 완료");
    }
}
