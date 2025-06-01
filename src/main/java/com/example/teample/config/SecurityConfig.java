package com.example.teample.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.example.teample.user.security.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;

    // 🔐 스프링 시큐리티 필터 체인을 설정하는 메서드
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 🔒 CSRF 보안 비활성화 (API 개발시에는 일반적으로 비활성화)
                .csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                // 🔓 요청 URL 별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // /api/users/** 경로는 인증 없이 접근 허용
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        // 그 외의 모든 요청은 인증 필요
                        .anyRequest().authenticated() // 나중에 지워야한다면 지워도 됨
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

                // 🔐 기본 인증 방식 사용 (Authorization: Basic ... 헤더)

        return http.build(); // 필터 체인 객체 반환
    }

    // 🔐 비밀번호 암호화를 위한 PasswordEncoder 빈 등록 (BCrypt 사용)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Spring Security가 내부적으로 사용하는 인증 관리자 Bean 등록
        return config.getAuthenticationManager();
    }

}
