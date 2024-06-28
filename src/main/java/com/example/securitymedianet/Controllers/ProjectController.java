package com.example.securitymedianet.Controllers;

import com.example.securitymedianet.Entites.Project;
import com.example.securitymedianet.Entites.ProjectStatus;
import com.example.securitymedianet.Services.Project.ProjectServices;
import lombok.RequiredArgsConstructor;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projet")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectServices  projectServices;

    @GetMapping("/odoo")
    public List<Project> getAllProjects() throws MalformedURLException, XmlRpcException {
        return projectServices.GetProjectFromOdoo();
    }

    @PutMapping("/update/")
   public Project updateProject(@RequestBody Project project){
        return projectServices.updateProject(project);
    }

    @PostMapping("/create/{id}")
    public Project createProject(@PathVariable Integer id,@RequestBody Project project){
        return projectServices.CreateProject(id,project);
    }

    @PutMapping("/status")
   public void checkStatus(){
   projectServices.checkStatus();
    }
    @GetMapping("/bystatus/{status}")
    public List<Project> getProjectsByStatus(@PathVariable ProjectStatus status){
        return projectServices.getbyStatus(status);
    }
    @GetMapping("/all")
    public List<Project> allProjects() {
        return projectServices.getAllProjects();
    }
@GetMapping("/sd")
    public Map<String,Object> getProjectsBySd(){
        return projectServices.getProjectsSmallDetails();
}
    @GetMapping("/articleshours/{id}")
    public Map<String,Object> getArticlesByConsumedhours(@PathVariable("id") Integer id){
        return projectServices.getProjectArticlesConsumedHours(id);
    }
    @GetMapping("/bytype/{type}")
    public List<Project> getProjectsByType(@PathVariable("type") String type){
        return projectServices.getbyType(type);
    }

    @GetMapping("/productivity")
    public Map<String,Object> getProjectsByProductivity(){
        return projectServices.getAllProjectProductivity();
    }

}
