package com.example.teample.project.repository;

import com.example.teample.project.domain.ProjectUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    List<ProjectUser> findByUser_UserId(Long userId);
}