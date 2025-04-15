package com.example.teample.user.controller;


import com.example.teample.user.domain.User;
import com.example.teample.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") //기본 url 경로 지정
@RequiredArgsConstructor //final로 선언된 필드를 자동으로 생성자 주입
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) { //사용자 요청을 받은 후 DB에 저장하고 저장된 User반환
        return userService.create(user);
    }

    @GetMapping
    public List<User> getUsers() { //모든 사용자 목록 조회해서 JSON으로 반환
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) { //특정 ID를 가진 사용자 조회
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) { //특정 사용자 삭제
        userService.delete(id);
    }
}
