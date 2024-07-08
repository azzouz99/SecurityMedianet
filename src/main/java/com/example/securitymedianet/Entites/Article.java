package com.example.securitymedianet.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date_début;
    private Date date_fin;
    private String ressources;
    private Float j_Vendus;
    private Float coût_unitaire;
    private Float consommés_H;
    private Float consommés_J;
    private Float J_Restant;
    private Float RAF;
    private Float Budget_Additionnel;
    private Float attérissage;
    private Float Marge;
    private Float Budget;
    private Float couts;
    private Float Marge_en_montant;
    private Float Marge_en_percent;
    private Float satisfaction_score;
  private ArticleStatus status;
  private boolean seen;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private Notification notification;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Project project;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(ressources, article.ressources) &&
                Float.compare(article.j_Vendus, j_Vendus) == 0 &&
                Float.compare(article.coût_unitaire, coût_unitaire) == 0 &&
                Float.compare(article.consommés_H, consommés_H) == 0 &&
                Float.compare(article.consommés_J, consommés_J) == 0 &&
                Float.compare(article.J_Restant, J_Restant) == 0 &&
                Float.compare(article.RAF, RAF) == 0 &&
                Float.compare(article.Budget_Additionnel, Budget_Additionnel) == 0 &&
                Float.compare(article.attérissage, attérissage) == 0 &&
                Float.compare(article.Marge, Marge) == 0 &&
                Float.compare(article.Budget, Budget) == 0 &&
                Float.compare(article.couts, couts) == 0 &&
                Float.compare(article.Marge_en_montant, Marge_en_montant) == 0 &&
                Float.compare(article.Marge_en_percent, Marge_en_percent) == 0 &&
                Float.compare(article.satisfaction_score, satisfaction_score) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ressources, j_Vendus, coût_unitaire, consommés_H, consommés_J, J_Restant, RAF, Budget_Additionnel, attérissage, Marge, Budget, couts, Marge_en_montant, Marge_en_percent, satisfaction_score);
    }
}
