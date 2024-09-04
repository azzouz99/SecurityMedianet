package com.example.securitymedianet.Entites.Analyse;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ArticleAnalysis {

    private String title;
    private Double totalmarge;
    private Double totaladddays;
    private Double ratio_r;
    private Double ratio_p;
    public ArticleAnalysis(String title, Double totalmarge, Double totaladddays) {
        this.title = title;
        this.totalmarge = totalmarge;
        this.totaladddays = totaladddays;
    }
    public ArticleAnalysis(String ressources, double Marge_en_montant, double Budget_Additionnel, double Marge_en_percent_avg, double attérissage_per_j_Vendus) {
        this.title = ressources;
        this.totalmarge = Marge_en_montant;
        this.totaladddays = Budget_Additionnel;
        this.ratio_r = Marge_en_percent_avg;
        this.ratio_p = attérissage_per_j_Vendus;
    }

}
