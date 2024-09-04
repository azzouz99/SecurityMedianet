package com.example.securitymedianet.Services.Project;

import com.example.securitymedianet.Entites.*;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.NotificationRepository;
import com.example.securitymedianet.Repositories.ProjectRepository;
import com.example.securitymedianet.Repositories.UserRepository;
import com.example.securitymedianet.Services.Article.ArticleService;
import com.example.securitymedianet.Services.Article.IArticleService;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Math.round;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@Service
public class ProjectServices implements IProjectServices {
    @Autowired
    private UserRepository userRepository;
@Autowired
    private  ProjectRepository projectRepository;

    @Autowired
    private ArticleRepository articleService;

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public Project updateProject(Project project){
      return   projectRepository.save(project);
    }
    @Override
    public  List<Project> getProjectByStatus(ProjectStatus status){
        return projectRepository.findProjectsByStatus(status);
    }
    @Override
    public  List<Project> getProjectByType(String type){
        return projectRepository.findByType(type);
    }
    @Override
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }
    @Override
    public Project getProject(Integer id){
        return projectRepository.findById(id).orElse(null);
    }
    @Override
    public Project CreateProject(Integer clientId,Project project){
        User client=userRepository.findById(clientId).orElse(null);
        List<Project> projects=client.getProjects();
        if(projects.isEmpty()){
            projects=new ArrayList<>();
            projects.add(project);
        } else {
            projects.add(project);
        }
        project.setClient(client);
        project.setStatus(ProjectStatus.ANALYSE);
        userRepository.save(client);
        return   projectRepository.save(project);
    }
    @Override
    public List<Project> GetProjectFromOdoo() throws MalformedURLException, XmlRpcException {
        final XmlRpcClient client = new XmlRpcClient();

        final XmlRpcClientConfigImpl start_config = new XmlRpcClientConfigImpl();
        start_config.setServerURL(new URL("https://demo.odoo.com/start"));
        final Map<String, String> info = (Map<String, String>)client.execute(
                start_config, "start", emptyList());

        final String url = info.get("host"),
                db = info.get("database"),
                username = info.get("user"),
                password = info.get("password");
        System.out.println("this is my database"+db);

        final XmlRpcClientConfigImpl common_config = new XmlRpcClientConfigImpl();
        common_config.setServerURL(new URL(String.format("%s/xmlrpc/2/common", url)));
        client.execute(common_config, "version", emptyList());

        int uid = (int)client.execute(common_config, "authenticate", asList(db, username, password, emptyMap()));

        final XmlRpcClient models = new XmlRpcClient() {{
            setConfig(new XmlRpcClientConfigImpl() {{
                setServerURL(new URL(String.format("%s/xmlrpc/2/object", url)));
            }});
        }};
        models.execute("execute_kw", asList(
                db, uid, password,
                "res.partner", "check_access_rights",
                asList("read"),
                new HashMap() {{ put("raise_exception", false); }}
        ));

        asList((Object[])models.execute("execute_kw", asList(
                db, uid, password,
                "res.partner", "search",
                asList(asList(
                        asList("is_company", "=", true)))
        )));
        List<Project> projects = new ArrayList<Project>();
        return projects;
    }
    @Override
    public void checkStatus(){
        List<Project> projects = projectRepository.findAll();

        for(Project project:projects) {
            List<Article> articles = articleService.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);
            if(articles.isEmpty()){
                project.setStatus(ProjectStatus.COMPLETED);
            }else {
                project.setStatus(ProjectStatus.INPROGRESS);
            }
            projectRepository.save(project);
        }
        }
    @Override
    public Map<String,Object> getProjectsSmallDetails() {
        Map<String,Object> map = new HashMap<>();
        List<Article> articles=articleService.findAll();
        Float lost_day=0F;
        Float win_day=0F;

        Long project_number=projectRepository.count();
        Long article_number=articleService.count();
        Long inprogress_project=projectRepository.countProjectsByStatus(ProjectStatus.INPROGRESS);
        Long completed_project=projectRepository.countProjectsByStatus(ProjectStatus.COMPLETED);
        Long inprogress_articles=articleService.countArticlesByStatus(ArticleStatus.IN_PROGRESS);
        Long completed_articles=articleService.countArticlesByStatus(ArticleStatus.COMPLETED);
        List<Notification> notificationList=notificationRepository.findByStatus(StatusNotif.NOT_YET);
        Integer nbrdes=notificationList.size();
        Float couts=0.0f;
        Float perte=0.0f;
        Float budget=0.0f;
        Float  consumedDay=0.0f;
        Float  soldDays=0.0f;
        Float necessaryDay=0.0f;
        Float raf=0F;
        for (Article article:articles) {
            raf+=article.getRAF();
            couts+=article.getCouts();
            budget+=article.getBudget();
            consumedDay+=article.getConsommés_J();
            necessaryDay+=article.getAttérissage();
            soldDays+=article.getJ_Vendus();
            if(article.getMarge_en_montant()<0){
                perte+=article.getMarge_en_montant();
            }
            if(article.getBudget_Additionnel()<0){
                lost_day+=article.getBudget_Additionnel();
            }
            if(article.getBudget_Additionnel()>0){
                win_day+=article.getBudget_Additionnel();
            }
        }

        map.put("couts",couts);
        map.put("budget",budget);
        map.put("marge",budget-couts);
        map.put("perte",perte);
        map.put("decision",nbrdes);
        Float perte_of_win=((budget-couts)/couts)*100;
        String formattedNumber = String.format("%.2f", perte_of_win);
        map.put("percent of win",formattedNumber);
        map.put("RAF",raf);
        map.put("win day",win_day);
        map.put("lost day",lost_day);
        map.put("Consummed Days",consumedDay);
        map.put("Necessary Days",necessaryDay);
        map.put("Sold Days",soldDays);
        map.put("productivity",String.format("%.2f", (soldDays/necessaryDay)*100));
        map.put("project number",project_number);
        map.put("article number",article_number);
        map.put("in_progress project",inprogress_project);
        map.put("completed project",completed_project);
        map.put("in_progress articles",inprogress_articles);
        map.put("completed articles",completed_articles);
      //map.put("expected Days",);


        return map;
    }
    @Override
    public Map<String,Object> getProjectArticlesConsumedHours(Integer id){
      Project project=  projectRepository.findById(id).orElse(null);
      List<Article> articles=project.getArticles();
      Map<String,Object> map = new HashMap<>();
      for(Article article:articles){
          map.put(article.getRessources(),article.getAttérissage());
      }
      return map;
    }
    @Override
    public Map<String,Object> getAllProjectProductivity(){
        Map<String,Object> map = new HashMap<>();
      List<Project> projects= projectRepository.findAll();
      for (Project project:projects){
            map.put(project.getName(),getProjectArticlesConsumedHours(project.getId()));
        }
        return map;
    }
    @Override
    public List<Project> findByTypeAndStatus(String type, ProjectStatus status){
      return    projectRepository.findByTypeAndStatus(type, status);
    }




    }

