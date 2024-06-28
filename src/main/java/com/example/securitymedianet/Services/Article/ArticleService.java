package com.example.securitymedianet.Services.Article;

import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;
import com.example.securitymedianet.Entites.ProjectStatus;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.ProjectRepository;
import com.example.securitymedianet.Services.Flask.FlaskServices;
import com.example.securitymedianet.Services.Project.IProjectServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService implements IArticleService{
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private FlaskServices flaskServices;
    @Autowired
    private IProjectServices projectServices;
    @Override
    public void updateStatus(){
        List<Article> articles=articleRepository.findAll();
        for(Article article:articles){
            if (article.getRAF()!=0){
                article.setStatus(ArticleStatus.IN_PROGRESS);
            }else {
                article.setStatus(ArticleStatus.COMPLETED);
            }
            articleRepository.save(article);
        }
    }
    @Override
    public List<Article> getAllArticles(){
        return articleRepository.findAll();
    }
    @Override
    public Map<String,Object> predictArticlesInprojectdrupal(Integer projectID){
        Map<String,Object> map=new HashMap<>();
        Project project = projectRepository.findById(projectID).get();
        List<Article> articles=articleRepository.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);

        logger.info("articles:",articles.size());
    Map<String,Object> map1=projectServices.getProjectArticlesConsumedHours(projectID);
        for(Article article:articles){
                if (article.getRessources().equals("Infographie ")){
                    Map<String,Object> prediction=flaskServices.predictInfographie(
                    ((Number) map1.get("Analyste concepteur")).floatValue(),
                    ((Number) map1.get("Insertion contenu")).floatValue());
                    map.put("Infographie ",((Number)prediction.get("prediction")).floatValue());
          //          map.put("Infographie ",map1.get("Infographie "));
                }
                if (article.getRessources().equals("Intégration")){

                    Map<String,Object> prediction=flaskServices.predictIntegration(
                            ((Number) map1.get("Ingénieur test")).floatValue(),
                            ((Number) map1.get("Ingénieur système")).floatValue(),
                            ((Number) map1.get("Infographie ")).floatValue());
                    map.put("Intégration",((Number)prediction.get("prediction")).floatValue());
          //          map.put("Intégration",map1.get("Intégration"));
                }
                if (article.getRessources().equals("Insertion contenu")){
                    Map<String,Object> prediction=flaskServices.predictIC(
                            ((Number) map1.get("Analyste concepteur")).floatValue(),
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Insertion contenu",((Number)prediction.get("prediction")).floatValue());
                   // map.put("Insertion contenu",map1.get("Insertion contenu"));
                }
                if (article.getRessources().equals("Ingénieur test")){
                    Map<String,Object> prediction=flaskServices.predictIT(
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue(),
                            ((Number) map1.get("Ingénieur système")).floatValue());
                    map.put("Ingénieur test",((Number)prediction.get("prediction")).floatValue());
      //              map.put("Ingénieur test",map1.get("Ingénieur test"));
                }
                if (article.getRessources().equals("Ingénieur système")){
                    Map<String,Object> prediction=flaskServices.predictIS(
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Ingénieur système",((Float)prediction.get("prediction")).floatValue());
          //          map.put("Ingénieur système",map1.get("Ingénieur système"));
                }
                if (article.getRessources().equals("Consultant SEO")){
                    Map<String,Object> prediction=flaskServices.predictSEO(
                            ((Number) map1.get("Analyste concepteur")).floatValue(),
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Consultant SEO",((Number)prediction.get("prediction")).floatValue());
      //              map.put("Consultant SEO",map1.get("Consultant SEO"));
                }
                if (article.getRessources().equals("Formation")){
                    Map<String,Object> prediction=flaskServices.predictFormation(
                            ((Number) map1.get("Gestion et coordination du projet")).floatValue());
                    map.put("Formation",((Number)prediction.get("prediction")).floatValue());
        //            map.put("Formation",map1.get("Formation"));
                }
        }

       if (map.isEmpty()){
           map.put("Nothing found",articles.size());
       }
        return map;
    }
    public Map<String,Object> predictArticlesInAllprojectdrupal(){
    List<Project> projects=projectRepository.findByType("DRUPAL");
    Map<String,Object> map=new HashMap<>();
    for(Project project:projects){
        if (project.getStatus().equals(ProjectStatus.INPROGRESS)) {
            map.put(project.getName(), predictArticlesInprojectdrupal(project.getId()));
        }
    }
   return map;
    }
    @Override
    public List<Article> findByStatus(ArticleStatus status){
        return articleRepository.findByStatus(status);
    }
    @Override
    public Map<String,Object> findByInprogressDrupal(){
       List<Project> projects=projectRepository.findByTypeAndStatus("DRUPAL", ProjectStatus.INPROGRESS);
       Map<String,Object> map=new HashMap<>();
       for(Project project:projects){
           map.put(project.getName(),articlesInprojectdrupal(project.getId()));
       }
       return map;
    }
    public Map<String,Object> articlesInprojectdrupal(Integer projectID){
        Map<String,Object> map=new HashMap<>();
        Project project = projectRepository.findById(projectID).get();
        List<Article> articles=articleRepository.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);


        Map<String,Object> map1=projectServices.getProjectArticlesConsumedHours(projectID);
        for(Article article:articles){
            if (article.getRessources().equals("Infographie ")){

                map.put("Infographie ",map1.get("Infographie "));
            }
            if (article.getRessources().equals("Intégration")){


                map.put("Intégration",map1.get("Intégration"));
            }
            if (article.getRessources().equals("Insertion contenu")){

                map.put("Insertion contenu",map1.get("Insertion contenu"));
            }
            if (article.getRessources().equals("Ingénieur test")){

                map.put("Ingénieur test",map1.get("Ingénieur test"));
            }
            if (article.getRessources().equals("Ingénieur système")){

                map.put("Ingénieur système",map1.get("Ingénieur système"));
            }
            if (article.getRessources().equals("Consultant SEO")){

                map.put("Consultant SEO",map1.get("Consultant SEO"));
            }
            if (article.getRessources().equals("Formation")){

                map.put("Formation",map1.get("Formation"));
            }
        }

        if (map.isEmpty()){
            map.put("Nothing found",articles.size());
        }
        return map;
    }
    public Map<String,Object> articleTracking(Integer articleID){

        Map<String,Object> map1=new HashMap<>();
        Article article=articleRepository.findById(articleID).orElse(null);
        map1.put("Les jours consommées",article.getConsommés_J());
        map1.put("Le RAF",article.getRAF());

        return map1;
    }
    @Override
    public Map<String,Object> articleTrackingInproject(){
        Map<String,Object> map=new HashMap<>();
        List<Project> projects=projectRepository.findByTypeAndStatus("DRUPAL", ProjectStatus.INPROGRESS);
        for(Project project:projects){
        List<Article> articles=articleRepository.findByStatusAndProject(ArticleStatus.IN_PROGRESS,project);
            Map<String,Object> map1=new HashMap<>();
        for(Article article:articles){
            map1.put(article.getRessources(),articleTracking(article.getId()));

        }
      map.put(project.getName(),map1);
        }
        return map;
    }
    @Override
    public Article findByTitleAndProject(String title, Integer projectID){
        Project project = projectRepository.findById(projectID).orElse(null);
        return articleRepository.findByRessourcesAndProject(title,project);
    }
    @Override
    public Map<String,Object> predict(Integer articleID) {
        Article article=articleRepository.findById(articleID).orElse(null);
        Map<String,Object> map=new HashMap<>();
       Project project=article.getProject();

            if (article.getRessources().equals("Infographie ")) {
                float analysteConcepteur = findByTitleAndProject("Analyste concepteur", project.getId()).getAttérissage();
                float insertion_contenu = findByTitleAndProject("Insertion contenu", project.getId()).getAttérissage();
                 map = flaskServices.predictInfographie(analysteConcepteur, insertion_contenu);

            }
            if (article.getRessources().equals("Intégration")){

                float ingenieur_test=findByTitleAndProject("Ingénieur test", project.getId()).getAttérissage();
                float ingenieur_systeme=findByTitleAndProject("Ingénieur système", project.getId()).getAttérissage();
                float infographie=findByTitleAndProject("Infographie ", project.getId()).getAttérissage();
                 map = flaskServices.predictIntegration(ingenieur_test,ingenieur_systeme,infographie);

            }
            if (article.getRessources().equals("Insertion contenu")){
                float analysteConcepteur=findByTitleAndProject("Analyste concepteur", project.getId()).getAttérissage();
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                map = flaskServices.predictIC(analysteConcepteur,gestionCoordination);

            }
            if (article.getRessources().equals("Ingénieur test")){
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                float ingenieur_systeme=findByTitleAndProject("Ingénieur système", project.getId()).getAttérissage();
               map = flaskServices.predictIT(gestionCoordination,ingenieur_systeme);

            }
            if (article.getRessources().equals("Ingénieur système")){
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                map = flaskServices.predictIS(gestionCoordination);

            }
            if (article.getRessources().equals("Consultant SEO")){
                float analysteConcepteur=findByTitleAndProject("Analyste concepteur", project.getId()).getAttérissage();
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
               map = flaskServices.predictSEO(analysteConcepteur,gestionCoordination);

            }
            if (article.getRessources().equals("Formation")){
                float gestionCoordination=findByTitleAndProject("Gestion et coordination du projet", project.getId()).getAttérissage();
                map = flaskServices.predictFormation(gestionCoordination);
            }

return map;

    }
}
