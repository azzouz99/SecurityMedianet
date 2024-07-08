package com.example.securitymedianet.Services.Notification;

import com.example.securitymedianet.Entites.*;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.NotificationRepository;
import com.example.securitymedianet.Services.Article.ArticleService;
import com.example.securitymedianet.Services.Flask.FlaskServices;
import com.example.securitymedianet.Services.Project.ProjectServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServices implements INotificationServices {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ProjectServices projectServices;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    ArticleService articleService;
    @Autowired
    private FlaskServices flaskServices;
    @Override
   public Article getArticle(Long notificationId){
        Notification notification =notificationRepository.findById(notificationId).orElse(null);
        return notification.getArticle();

    }
    @Override
    public void checkInprogress() {
        List<Article> articles = articleService.findByStatus(ArticleStatus.IN_PROGRESS);
        for (Article article : articles) {
            Project project = article.getProject();
            if ((project.getType().equals("DRUPAL"))
                    &&(!article.getRessources().equals("Gestion et coordination du projet"))
                    &&(!article.getRessources().equals("Analyste concepteur")))
            {
            Map<String,Object> map=articleService.predict(article.getId());
/*
                if ((article.getAttérissage() > ((Number) map.get("prediction")).floatValue())&&(article.getConsommés_J()<article.getJ_Vendus())) {
                    Notification notification = article.getNotification();
                    if (article.getNotification()==null){
                        notification = new Notification();
                    }
                    notification.setTitle("Mauvaise Planification");
                    notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une mauvaise planification" +
                            " dans l'article " + article.getRessources() + "puisque on a prédicté qu'il y a besoin de" + ((Number) map.get("prediction")).floatValue() + " Jours" +
                            "mais on a mettre que  " + article.getAttérissage()+" Jours.\n Il vous reste que "+article.getJ_Restant()+" jours pour dépasser les jours vendus");
                    notification.setProductivity(NotificationDegree.Warning);
                    notification.setArticle(article);
                    article.setNotification(notification);
                    articleRepository.save(article);
                }
                */
                if (article.getConsommés_J() > article.getJ_Vendus()) {
                    Notification notification = article.getNotification();
                    if (article.getNotification()==null){
                        notification = new Notification();
                        if (article.getMarge_en_montant()>=0){
                            notification.setTitle("Une perte du marge");
                            notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une perte" +
                                    " dans l'article " + article.getRessources() + "puisque on a vendu" + article.getJ_Vendus()+ " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J()+" Jours.\n" +"La perte financiére maintenant est"+article.getJ_Restant()*article.getCouts()
                            +" DT Le pourcentage du marge est devenir  "
                                    +article.getMarge_en_montant()/article.getCouts()*100+" %.");
                            notification.setProfitability(NotificationDegree.Warning);

                        }else {
                            notification.setTitle("Une perte de "+article.getJ_Restant()*article.getCouts() +" DT");
                            notification.setDescription("Une perte de " +article.getMarge_en_montant()+
                                    " DT puisque on a vendu" + article.getJ_Vendus()+ " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J()+" Jours.\n  Le projet a une pourcentage de perte de "
                                    +article.getMarge_en_montant()/article.getCouts()*100+" %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    }else {
                        if (article.getMarge_en_montant() >= 0) {
                            notification.setTitle(notification.getTitle() + "et une perte du marge");
                            notification.setDescription(notification.getDescription()+"et  une perte du marge" +
                                    article.getCouts()*article.getJ_Restant() + " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le pourcentage du marge est devenir  "
                            +article.getMarge_en_montant()/article.getCouts()*100+" %.");

                            notification.setProfitability(NotificationDegree.Warning);

                        } else {
                            notification.setTitle(notification.getTitle() +"et une perte de " + article.getJ_Restant() * article.getCouts() + " DT");
                            notification.setDescription("Une perte de " + article.getMarge_en_montant() +
                                    " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le projet a une pourcentage de perte de "
                            +article.getMarge_en_montant()/article.getCouts()*100+" %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    }

                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);

                }
                if ((article.getJ_Vendus() < ((Number) map.get("prediction")).floatValue())&&(article.getConsommés_J()<article.getJ_Vendus())) {

                    Notification notification = article.getNotification();
                    if (article.getNotification()==null){
                        notification = new Notification();
                    }
                    notification.setTitle("Risque de perte dans l'article "+article.getRessources()+" du "+article.getProject().getName());
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à un risque de perte" +
                            " dans l'article " + article.getRessources() + " puisque on a prédicté que il a besoin de " + ((Number) map.get("prediction")).floatValue() + " Jours" +
                            " mais on a vendu que " + article.getJ_Vendus()+" Jours.");
                    notification.setProfitability(NotificationDegree.Warning);
                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);
                }


            }
            else {
                if ((article.getAttérissage() > article.getJ_Vendus())&&(article.getConsommés_J()<article.getJ_Vendus())) {
                    Notification notification = article.getNotification();
                    if (article.getNotification()==null){
                        notification = new Notification();
                    }

                    notification.setTitle("Risque de perte");
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à un risque de perte" +
                            " dans l'article " + article.getRessources() + " puisque il a demandé " + article.getAttérissage() + " Jours" +
                            " mais on a vendu que " + article.getJ_Vendus()+" Jours.");
                    notification.setProductivity(NotificationDegree.Warning);
                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);
                }
                if (article.getConsommés_J() > article.getJ_Vendus()) {
                    Notification notification = article.getNotification();
                    if (article.getNotification()==null){
                        notification = new Notification();
                        if (article.getMarge_en_montant()>=0){
                            notification.setTitle("Une perte du marge");
                            notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une perte" +
                                    " dans l'article " + article.getRessources() + "puisque on a vendu" + article.getJ_Vendus()+ " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J()+" Jours.\n" +"La perte financiére maintenant est"+article.getJ_Restant()*article.getCouts()
                                    +" DT Le pourcentage du marge est devenir  "
                                    +article.getMarge_en_montant()/article.getCouts()*100+" %.");
                            notification.setProfitability(NotificationDegree.Warning);

                        }else {
                            notification.setTitle("Une perte de "+article.getJ_Restant()*article.getCouts() +" DT");
                            notification.setDescription("Une perte de " +article.getMarge_en_montant()+
                                    " DT puisque on a vendu" + article.getJ_Vendus()+ " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J()+" Jours.\n  Le projet a une pourcentage de perte de "
                                    +article.getMarge_en_montant()/article.getCouts()*100+" %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    }else {
                        if (article.getMarge_en_montant() >= 0) {
                            notification.setTitle(notification.getTitle() + "et une perte du marge");
                            notification.setDescription(notification.getDescription()+"et  une perte du marge" +
                                    article.getCouts()*article.getJ_Restant() + " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le pourcentage du marge est devenir  "
                                    +article.getMarge_en_montant()/article.getCouts()*100+" %.");

                            notification.setProfitability(NotificationDegree.Warning);

                        } else {
                            notification.setTitle(notification.getTitle() +"et une perte de " + article.getJ_Restant() * article.getCouts() + " DT");
                            notification.setDescription("Une perte de " + article.getMarge_en_montant() +
                                    " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le projet a une pourcentage de perte de "
                                    +article.getMarge_en_montant()/article.getCouts()*100+" %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    }

                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);

                }
            }
        }
    }

    @Override
    public void checkPerformance(){
        List<Article> articles = articleService.findByStatus(ArticleStatus.COMPLETED);

        for (Article article : articles) {
          //  if(!article.isRead()){
            Notification notification = new Notification();
            notification.setArticle(article);
            article.setNotification(notification);
            if (article.getAttérissage().equals(article.getJ_Vendus())){
                notification.setTitle("Productivité equilibré");
                notification.setDescription("L'article "+article.getRessources()+" du "+article.getProject().getName()+" a une productivité equilibré avec des jours" +
                        " consommés equivaut aux jours vendus de "+article.getJ_Vendus()+" Jours.");
                notification.setProductivity(NotificationDegree.Successful);


            }
            if (article.getAttérissage()> article.getJ_Vendus()){
                notification.setTitle("Productivité faible");
                notification.setDescription("L'article "+article.getRessources()+" du "+article.getProject().getName()+" a une productivité faible avec des jours" +
                        " perdus  de "+article.getJ_Restant()+" Jours.");
                notification.setProductivity(NotificationDegree.Dangerous);

            }
            if (article.getAttérissage()< article.getJ_Vendus()){
                notification.setTitle("Productivité excellente");
                notification.setDescription("L'article "+article.getRessources()+" du "+article.getProject().getName()+" a une productivité éxcéllente avec des jours" +
                        " additionnels  de "+article.getJ_Restant()+" Jours.");
                notification.setProductivity(NotificationDegree.Successful);
            }
            articleRepository.save(article);
}

     //   }
}
    @Override
    public void checkRentability(float percentage){
    List<Article> articles = articleService.findByStatus(ArticleStatus.COMPLETED);
    for (Article article : articles) {
    //    if(!article.isRead()){
        Notification notification = article.getNotification();
        String title = notification.getTitle();
        String description = notification.getDescription();

        float percentage_win = ((article.getBudget() - article.getCouts()) / article.getCouts()) * 100;
        if (percentage_win >= percentage) {
            notification.setTitle(title + " et article profitable");
            notification.setDescription(description + "\n Avec une profitabilité de " + percentage_win + " %.");
            notification.setProfitability(NotificationDegree.Successful);
        }
        if ((percentage_win < percentage) && (percentage_win >= 0)) {
            notification.setTitle(title + " et une rentabilité  faible");
            notification.setDescription(description + "\n Avec une rentabilité Insuffisante de " + percentage_win + " %.");
            notification.setProfitability(NotificationDegree.Normal);
        }
        if (percentage_win < 0) {
            notification.setTitle(title + " et une perte  d'argent");
            notification.setDescription(description + "\n Avec une rentabilité grave de " + percentage_win + "  perdu.");
            notification.setProfitability(NotificationDegree.Dangerous);
        }
     //   article.setRead(true);
        articleRepository.save(article);
    }
  //  }

    }
    @Override
    public void deleteNotification(Long id){
        notificationRepository.deleteById(id);
    }
    public Map<String,Object> AnalyseProject(Integer projectID){
    Project project=projectServices.getProject(projectID);
    Map<String,Object> map = new HashMap<>();

    return map;
    }
    public NotificationDegree getNotificationDegree(NotificationDegree first, NotificationDegree second){
    NotificationDegree result = null;
    if (first == second){
        result= first;
    }
    if ( ((first == NotificationDegree.Warning)&&(second== NotificationDegree.Dangerous))||((second == NotificationDegree.Warning)&&(first== NotificationDegree.Dangerous))){
        result= NotificationDegree.Dangerous;
    }
    if ( ((first == NotificationDegree.Successful)&&(second== NotificationDegree.Normal))||((second == NotificationDegree.Successful)&&(first== NotificationDegree.Normal))){
        result= NotificationDegree.Normal;
    }
    if ( ((first == NotificationDegree.Normal)&&(second== NotificationDegree.Warning))||((second == NotificationDegree.Warning)&&(first== NotificationDegree.Normal))){
            result= NotificationDegree.Warning;
    }
        if ( ((first == NotificationDegree.Successful)&&(second== NotificationDegree.Dangerous))||((second == NotificationDegree.Successful)&&(first== NotificationDegree.Dangerous))){
            result= NotificationDegree.Dangerous;
        }
        if ( ((first == NotificationDegree.Normal)&&(second== NotificationDegree.Dangerous))||((second == NotificationDegree.Successful)&&(first== NotificationDegree.Dangerous))){
            result= NotificationDegree.Dangerous;
        }
    return result;
    }
    @Override
    public List<Map<String, Object>> findForProjectInProgress(){
        List<Map<String, Object>> notifications = new ArrayList<>();
        List<Notification> all=notificationRepository.findAll();
        for (Notification notification : all){
            if (notification.getArticle().getStatus().equals(ArticleStatus.IN_PROGRESS)){
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("id",notification.getId());
                notificationData.put("title",notification.getTitle());
                notificationData.put("description",notification.getDescription());
                notificationData.put("productivity",notification.getProductivity());
                notificationData.put("profitability",notification.getProfitability());
                notificationData.put("projectName",notification.getArticle().getProject().getName());
                notificationData.put("articleName",notification.getArticle().getRessources());
                notifications.add(notificationData);
            }
        }

        return notifications;
    }
    @Override
    public List<Map<String, Object>> findForProjectCompleted(){
        List<Map<String, Object>> notifications = new ArrayList<>();
        List<Notification> all=notificationRepository.findAll();
        for (Notification notification : all){
            if (notification.getArticle().getStatus().equals(ArticleStatus.COMPLETED)){
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("id",notification.getId());
                notificationData.put("title",notification.getTitle());
                notificationData.put("description",notification.getDescription());
                notificationData.put("productivity",notification.getProductivity());
                notificationData.put("profitability",notification.getProfitability());
                notificationData.put("projectName",notification.getArticle().getProject().getName());
                notificationData.put("articleName",notification.getArticle().getRessources());
                notifications.add(notificationData);
            }
        }

        return notifications;
    }

    public void predictionCheck(){
        List<Article> articles = articleService.findByStatus(ArticleStatus.IN_PROGRESS);

        for (Article article : articles) {
            //  if(!article.isRead()){
            Notification notification = new Notification();
            notification.setArticle(article);
            article.setNotification(notification);
            if (article.getAttérissage().equals(article.getJ_Vendus())){
                notification.setTitle("Productivité equilibré");
                notification.setDescription("L'article "+article.getRessources()+" du "+article.getProject().getName()+" a une productivité equilibré avec des jours" +
                        " consommés equivaut aux jours vendus de "+article.getJ_Vendus()+" Jours.");
                notification.setProductivity(NotificationDegree.Successful);


            }
            if( (article.getAttérissage()> article.getJ_Vendus())&&(article.getConsommés_J()<article.getJ_Vendus())){
                notification.setTitle("Risque de perte");
                notification.setDescription("L'article "+article.getRessources()+" du "+article.getProject().getName()+" a une productivité faible avec des jours" +
                        " perdus  de "+article.getJ_Restant()+" Jours.");
                notification.setProductivity(NotificationDegree.Dangerous);

            }
            if (article.getAttérissage()< article.getJ_Vendus()){
                notification.setTitle("Productivité excellente");
                notification.setDescription("L'article "+article.getRessources()+" du "+article.getProject().getName()+" a une productivité éxcéllente avec des jours" +
                        " additionnels  de "+article.getJ_Restant()+" Jours.");
                notification.setProductivity(NotificationDegree.Successful);
            }
            articleRepository.save(article);
        }

    }
@Override
    public Map<String,Object> getPerformanceCompleted(){
        Map<String, Object> performanceCompleted = new HashMap<>();
        Map<String,Object> productivity = new HashMap<>();
        Map<String,Object> profitability = new HashMap<>();
        List<Article> articles=articleService.findByStatus(ArticleStatus.COMPLETED);
        Integer prod_dang=0;
        Integer prod_suc=0;
        Integer prod_normal=0;
        Integer prof_dang=0;
        Integer prof_suc=0;
        Integer prof_normal=0;
        for (Article article : articles){
            Notification notification = article.getNotification();
            if (notification.getProductivity().equals(NotificationDegree.Dangerous)){
                prod_dang++;
            }
            if (notification.getProductivity().equals(NotificationDegree.Successful)){prod_suc++;}

            if (notification.getProductivity().equals(NotificationDegree.Normal)){prod_normal++;}

            if (notification.getProfitability().equals(NotificationDegree.Dangerous)){
                prof_dang++;
            }
            if (notification.getProfitability().equals(NotificationDegree.Successful)){prof_suc++;}

            if (notification.getProfitability().equals(NotificationDegree.Normal)){prof_normal++;}
        }
        productivity.put("Mauvaise",prod_dang);
        productivity.put("Excellente",prod_suc);
        productivity.put("Normal",prod_normal);

        profitability.put("Excellente",prof_suc);
        profitability.put("Normal",prof_normal);
        profitability.put("Mauvaise",prof_dang);
        performanceCompleted.put("Rentabilité",profitability);
        performanceCompleted.put("Productivité",productivity);
        return performanceCompleted;
        }




}
