package com.example.teample.project.dto;


import lombok.Getter;

@Getter
public class ProjectRequestDto {
    private String projectName;
    private String role; //권한 (팀장, 팀원)

}
