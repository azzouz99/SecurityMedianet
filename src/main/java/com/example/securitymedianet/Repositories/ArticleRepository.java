package com.example.securitymedianet.Repositories;

import com.example.securitymedianet.Entites.Analyse.ArticleAnalysis;
import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.ArticleStatus;
import com.example.securitymedianet.Entites.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    @Query("SELECT COUNT(p) FROM Article p WHERE p.status = :status")
    long countArticlesByStatus(@Param("status") ArticleStatus status);

    List<Article> findByStatusAndProject(ArticleStatus status, Project project);

    List<Article> findByStatus(ArticleStatus status);

    Article findByRessourcesAndProject(String ressources, Project project);




    @Query("SELECT new com.example.securitymedianet.Entites.Analyse.ArticleAnalysis(a.ressources, SUM(a.Marge_en_montant), SUM(a.Budget_Additionnel)) " +
            "FROM Article a GROUP BY a.ressources")
    List<ArticleAnalysis> findArticleSummaries();

    @Query("SELECT new com.example.securitymedianet.Entites.Analyse.ArticleAnalysis(" +
            "a.ressources, " +
            "SUM(a.Marge_en_montant), " +
            "SUM(a.Budget_Additionnel), " +
            "(SUM(a.Marge_en_percent) / COUNT(a)), " +
            "CASE WHEN SUM(a.j_Vendus) = 0 THEN 0 ELSE SUM(a.j_Vendus) / SUM(a.attérissage) END" +
            ") " +
            "FROM Article a " +
            "GROUP BY a.ressources")
    List<ArticleAnalysis> findArticleDetails();
}
