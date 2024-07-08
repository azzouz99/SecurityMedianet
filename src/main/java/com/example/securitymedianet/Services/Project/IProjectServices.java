package com.example.securitymedianet.Services.Project;

import com.example.securitymedianet.Entites.Project;
import com.example.securitymedianet.Entites.ProjectStatus;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface IProjectServices {
    Project updateProject(Project project);

    List<Project> getProjectByStatus(ProjectStatus status);

    List<Project> getProjectByType(String type);

    List<Project> getAllProjects();

    Project getProject(Integer id);

    Project CreateProject(Integer clientId, Project project);

    List<Project> GetProjectFromOdoo() throws MalformedURLException, XmlRpcException;

    void checkStatus();

    Map<String,Object> getProjectsSmallDetails();


    Map<String,Object> getProjectArticlesConsumedHours(Integer id);

    Map<String,Object> getAllProjectProductivity();

    List<Project> findByTypeAndStatus(String type, ProjectStatus status);
}
