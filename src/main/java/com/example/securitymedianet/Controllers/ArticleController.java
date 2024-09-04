package com.example.securitymedianet.Controllers;

import com.example.securitymedianet.Entites.Analyse.ArticleAnalysis;
import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.ProjectRepository;
import com.example.securitymedianet.Services.Article.IArticleService;
import com.example.securitymedianet.Services.Notification.INotificationServices;
import com.example.securitymedianet.Services.Project.IProjectServices;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ProjectRepository projectRepository;


    private final ArticleRepository articleRepository;
    private final IProjectServices projectServices;
    private final IArticleService articleService;
    private final INotificationServices notificationServices;
    @GetMapping("/stpr/{id}/{status}")
  List<Article>  findByStatusAndProject(@PathVariable("status") ArticleStatus status,@PathVariable("id") Integer projectid) {
        Project project = projectRepository.findById(projectid).orElse(null);
        return articleRepository.findByStatusAndProject(status, project);
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        ResponseEntity<?> response=articleService.uploadFile(file);
        projectServices.checkStatus();
        notificationServices.articleScan();
     return response;
    }
    @PostMapping("/status")
    public void updateStatus(){
        articleService.updateStatus();
    }
    @GetMapping("/drupal/predictArticleinprogress/{id}")
    public Map<String,Object> predictArticlesInprojectdrupal(@PathVariable("id") Integer projectID){
      return   articleService.predictArticlesInprojectdrupal(projectID);
    }
    @GetMapping("/drupal/predictinprogress")
    public Map<String,Object> predictinprogress(){
        return articleService.predictArticlesInAllprojectdrupal();
    }
    @GetMapping("/drupal/inprogress")
    public Map<String,Object> inprogress(){
    return articleService.findByInprogressDrupal();
    }
    @GetMapping("/tracking")
    public Map<String,Object> tracking(){
        return articleService.articleTrackingInproject();
    }
    @GetMapping("/to/{id}")
    public Article getByTitleAndProject(@RequestParam String ressources,@PathVariable("id")Integer id){
        return articleService.findByTitleAndProject(ressources, id);
    }
    @GetMapping("/drupal/predict/{id}")
    public Map<String,Object> predict(@PathVariable Integer id){
        return articleService.predict(id);

    }
    @GetMapping("/byproject/{id}")
    public String getByProjectNamebyArtcile(@PathVariable("id") Integer articleID){
        return articleService.getProjectNamebyArticle(articleID);
    }

    @GetMapping("/article-analysis")
    public List<ArticleAnalysis> findArticleSummaries() {
        return articleService.findArticleDetails();
    }

    @DeleteMapping("/clear")
    public void deleteAllData(){
        articleService.deleteAllData();
    }
    @GetMapping("/export")
    public void exportHeadersToExcel(HttpServletResponse response) throws IOException {
        articleService.exportHeaders(response);
    }

}

