package com.example.securitymedianet.Services.Article;

import com.example.securitymedianet.Entites.Analyse.ArticleAnalysis;
import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IArticleService {
    void updateStatus();

    List<Article> getArticles();

    Map<String, Object> predictArticlesInprojectdrupal(Integer projectID);

    Map<String, Object> predictArticlesInAllprojectdrupal();

    List<Article> findByStatus(ArticleStatus status);

    Map<String, Object> findByInprogressDrupal();

    Map<String, Object> articleTrackingInproject();

    Article findByTitleAndProject(String title, Integer projectID);

    Map<String, Object> predict(Integer articleID);

    void exportHeaders(HttpServletResponse response) throws IOException;

    ResponseEntity<?> uploadFile(MultipartFile file);

    String getProjectNamebyArticle(Integer articleID);

    List<ArticleAnalysis> findArticleDetails();

    @Transactional
    void deleteAllData();
}
