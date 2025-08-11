package com.example.teample.project.dto;

import com.example.teample.project.domain.Project;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectResponseDto {
    private long projectId;
    private String projectName;

    public static ProjectResponseDto from(Project project) {
        return ProjectResponseDto.builder()
                .projectId(project.getProjectId())
                .projectName(project.getProjectName())
                .build();
    }
}
