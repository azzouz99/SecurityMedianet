package com.example.securitymedianet.Repositories;

import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;
import com.example.securitymedianet.Entites.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    Project findByName(String name);

    List<Project> findProjectsByStatus(ProjectStatus status);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    long countProjectsByStatus(@Param("status") ProjectStatus status);

    List<Project> findByType(String type);

    List<Project> findByTypeAndStatus(String type, ProjectStatus status);
}
