package com.example.teample.project.controller;

import com.example.teample.project.Service.ProjectService;
import com.example.teample.project.dto.ProjectRequestDto;
import com.example.teample.project.dto.ProjectResponseDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Validated
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 프로젝트 생성
     * - 세션에서 email을 꺼내 현재 로그인 사용자로 프로젝트 생성
     * - 생성자도 ProjectUser 로 참여(역할: dto.role)
     * 성공 시 201 Created + Location 헤더 반환
     */
    @PostMapping
    public ResponseEntity<ProjectResponseDto> create(
            @RequestBody ProjectRequestDto dto,
            HttpSession session
    ) {
        // 세션 체크 (로그인 보장)
        String email = (String) session.getAttribute("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        ProjectResponseDto created = projectService.createProject(dto, email);

        // 리소스 위치를 Location 헤더로 알려주면 REST 경험이 좋아짐
        URI location = URI.create("/api/projects/" + created.getProjectId());
        return ResponseEntity.created(location).body(created); // 201
    }

    /**
     * 내가 속한 프로젝트 목록 조회
     * - 세션의 email 기준으로 ProjectUser 관계에서 조회
     */
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getMyProjects(HttpSession session) {
        String email = (String) session.getAttribute("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        List<ProjectResponseDto> list = projectService.getProjectsByUser(email);
        return ResponseEntity.ok(list);
    }
}
