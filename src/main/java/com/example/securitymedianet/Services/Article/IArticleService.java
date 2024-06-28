package com.example.securitymedianet.Services.Article;

import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;

import java.util.List;
import java.util.Map;

public interface IArticleService {
    void updateStatus();

    List<Article> getAllArticles();

    Map<String,Object> predictArticlesInprojectdrupal(Integer projectID);

    List<Article> findByStatus(ArticleStatus status);

    Map<String,Object> findByInprogressDrupal();

    Map<String,Object> articleTrackingInproject();

    Article findByTitleAndProject(String title, Integer projectID);

    Map<String,Object> predict(Integer articleID);
}
