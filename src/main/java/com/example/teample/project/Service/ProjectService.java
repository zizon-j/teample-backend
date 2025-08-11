package com.example.teample.project.Service;

import com.example.teample.project.domain.Project;
import com.example.teample.project.domain.ProjectUser;
import com.example.teample.project.dto.ProjectRequestDto;
import com.example.teample.project.dto.ProjectResponseDto;
import com.example.teample.project.repository.ProjectRepository;
import com.example.teample.project.repository.ProjectUserRepository;
import com.example.teample.user.domain.User;
import com.example.teample.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto dto, String userEmail) {
        //세션 누락 방지
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalStateException("세션에 사용자 이메일이 없습니다. 로그인 후 다시 시도하세요.");
        }
        //유효성 검사
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. email=" + userEmail));

        // 1) 프로젝트 저장
        Project project = Project.builder()
                .projectName(dto.getProjectName())
                .build();
        projectRepository.save(project);

        // 2) 생성자를 ProjectUser로 참여시키기
        ProjectUser projectUser = ProjectUser.builder()
                .project(project)
                .user(user)
                .role(dto.getRole())     // "팀장" 등
                .build();
        projectUserRepository.save(projectUser);

        return ProjectResponseDto.from(project);
    }

    //내가 속한 프로젝트 목록 조회
    @Transactional(readOnly = true)
    public List<ProjectResponseDto> getProjectsByUser(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalStateException("세션에 사용자 이메일이 없습니다. 로그인 후 다시 시도하세요.");
        }

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다. email=" + userEmail));

        return projectUserRepository.findByUser_UserId(user.getUserId()).stream()
                .map(pu -> ProjectResponseDto.from(pu.getProject()))
                .distinct()
                .collect(Collectors.toList());
    }
}

