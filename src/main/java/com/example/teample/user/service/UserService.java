package com.example.teample.user.service;

import com.example.teample.user.domain.User;
import com.example.teample.user.dto.UserRequestDto;
import com.example.teample.user.dto.UserResponseDto;
import com.example.teample.user.repository.RoleRepository;
import com.example.teample.user.repository.UserRepository;
import com.example.teample.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public User getByEmail(String email) {
        Optional<User> user = userRepository.findByUserEmail(email);

        if(user.isEmpty())
            throw new NullPointerException("email is empty");

        return user.get();
    }

    public UserResponseDto create(UserRequestDto requestDto) {
        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("역할 없음"));

        log.info("user pw : {}, user encode pwd {}", requestDto.getUserPwd(), passwordEncoder.encode(requestDto.getUserPwd()));

        User user = User.builder()
                .userEmail(requestDto.getUserEmail())
                .userName(requestDto.getUserName())
                .userPassword(passwordEncoder.encode(requestDto.getUserPwd()))
                .userBirthday(requestDto.getUserBirthday())
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        return UserResponseDto.from(savedUser);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponseDto::from) //각 User 객체를 UserResponseDto로 변환함
                .collect(Collectors.toList()); //DTO로 변환된 요소들을 리스트로 다시 모아서 반환함
    }

    public UserResponseDto findById(Long id) {
        return UserResponseDto.from(
                userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("유저 없음"))
        );
    }

    @Transactional
    public UserResponseDto update(Long id, UserRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.setUserEmail(requestDto.getUserEmail());
        user.setUserName(requestDto.getUserName());
        user.setUserPassword(requestDto.getUserPwd());
        user.setUserBirthday(requestDto.getUserBirthday());

        //필요 시 Role도 업데이트
        Role role = roleRepository.findById(requestDto.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("역할 없음"));
        user.setRole(role);

        return UserResponseDto.from(user); // 저장은 @Transactional에 의해 자동 적용됨
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
