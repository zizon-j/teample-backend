package com.example.teample.user.controller;

import com.example.teample.user.dto.LoginRequestDto;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto, HttpSession session) {
        try {
            // 사용자 인증 시도: 사용자가 입력한 이메일/비밀번호로 인증 객체 생성
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );

            session.setAttribute("email", loginRequestDto.getEmail());

            //인증 성공 시
            return ResponseEntity.ok("로그인 성공");
        } catch (AuthenticationException e) {
            // 인증 실패 시 401 Unauthorized 반환
            return ResponseEntity.status(401).body("로그인 실패 : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("무언가 실패 : " + e.getMessage());
        }
    }
}
