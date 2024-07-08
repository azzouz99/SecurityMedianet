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
    public ArticleAnalysis(String title, Double totalmarge, Double totaladddays, Double ratio_p, Double ratio_r) {
        this.title = title;
        this.totalmarge = totalmarge;
        this.totaladddays = totaladddays;
        this.ratio_r = ratio_r;
        this.ratio_p = ratio_p;
    }

}
