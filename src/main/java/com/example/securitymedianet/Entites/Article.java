package com.example.securitymedianet.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "article")
public class Article {


    @Id
    @GeneratedValue
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
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "project_id")
    private Project project;



}
