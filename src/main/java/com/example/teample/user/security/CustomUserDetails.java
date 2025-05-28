package com.example.teample.user.security;

import lombok.Getter;
import com.example.teample.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    // 우리 프로젝트의 User 엔티티를 포함
    private final User user;

    // 생성자에서 User 객체를 받아서 초기화
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // ✅ 현재는 권한 목록 비워둠 (추후 Role 관리 필요시 여기서 반환)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // ex) List.of(new SimpleGrantedAuthority(user.getRole().getName()))
    }

    // ✅ Spring Security에서 비밀번호 비교할 때 사용
    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    // ✅ 로그인 시 사용할 ID 지정 (이메일을 username으로 사용)
    @Override
    public String getUsername() {
        return user.getUserEmail();
    }

    // ✅ 계정이 만료되지 않았는지 여부 (true면 사용 가능)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ✅ 계정이 잠겨 있지 않은지 여부 (true면 사용 가능)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ✅ 비밀번호가 만료되지 않았는지 여부 (true면 사용 가능)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ✅ 계정이 활성화 상태인지 여부 (true면 사용 가능)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
