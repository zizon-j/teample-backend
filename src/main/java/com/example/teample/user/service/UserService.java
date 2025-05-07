package com.example.teample.user.service;

import com.example.teample.user.domain.User;
import com.example.teample.user.dto.UserRequestDto;
import com.example.teample.user.dto.UserResponseDto;
import com.example.teample.user.repository.RoleRepository;
import com.example.teample.user.repository.UserRepository;
import com.example.teample.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserResponseDto create(UserRequestDto requestDto) {
        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("역할 없음"));


        User user = User.builder()
                .userEmail(requestDto.getUserEmail())
                .userName(requestDto.getUserName())
                .userPassword(requestDto.getUserPwd())
                .userBirthday(requestDto.getUserBirthday())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        return UserResponseDto.from(savedUser);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
