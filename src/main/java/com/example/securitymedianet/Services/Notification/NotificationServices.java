package com.example.securitymedianet.Services.Notification;

import com.example.securitymedianet.Entites.*;
import com.example.securitymedianet.Repositories.ArticleRepository;
import com.example.securitymedianet.Repositories.NotificationRepository;
import com.example.securitymedianet.Services.Article.ArticleService;
import com.example.securitymedianet.Services.Flask.FlaskServices;
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
    private ArticleRepository articleRepository;
    @Autowired
    ArticleService articleService;
    @Autowired
    private FlaskServices flaskServices;

    @Override
    public Article getArticle(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElse(null);
        return notification.getArticle();

    }

    @Override
    public void checkInprogress() {
        List<Article> articles = articleService.findByStatus(ArticleStatus.IN_PROGRESS);
        for (Article article : articles) {
            Project project = article.getProject();
            if ((project.getType().equals("DRUPAL"))
                    && (!article.getRessources().equals("Gestion et coordination du projet"))
                    && (!article.getRessources().equals("Analyste concepteur"))) {
                Map<String, Object> map = articleService.predict(article.getId());
                if (article.getConsommés_J() > article.getJ_Vendus()) {
                    Notification notification = article.getNotification();
                    if (article.getNotification() == null) {
                        notification = new Notification();
                        if (article.getMarge_en_montant() >= 0) {
                            notification.setTitle("Une perte du marge");
                            notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une perte" +
                                    " dans l'article " + article.getRessources() + "puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n" + "La perte financiére maintenant est" + article.getJ_Restant() * article.getCouts()
                                    + " DT Le pourcentage du marge est devenir  "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.");
                            notification.setProfitability(NotificationDegree.Warning);

                        } else {
                            notification.setTitle("Une perte de " + article.getJ_Restant() * article.getCouts() + " DT");
                            notification.setDescription("Une perte de " + article.getMarge_en_montant() +
                                    " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n  Le projet a une pourcentage de perte de "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    } else {
                        if (article.getMarge_en_montant() >= 0) {
                            notification.setTitle(notification.getTitle() + "et une perte du marge");
                            notification.setDescription(notification.getDescription() + "et  une perte du marge" +
                                    article.getCouts() * article.getJ_Restant() + " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le pourcentage du marge est devenir  "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.");

                            notification.setProfitability(NotificationDegree.Warning);

                        } else {
                            notification.setTitle(notification.getTitle() + "et une perte de " + article.getJ_Restant() * article.getCouts() + " DT");
                            notification.setDescription("Une perte de " + article.getMarge_en_montant() +
                                    " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le projet a une pourcentage de perte de "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    }

                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);

                }
                if ((article.getJ_Vendus() < ((Number) map.get("prediction")).floatValue()) && (article.getConsommés_J() < article.getJ_Vendus())) {

                    Notification notification = article.getNotification();
                    if (article.getNotification() == null) {
                        notification = new Notification();
                    }
                    notification.setTitle("Risque de perte dans l'article " + article.getRessources() + " du " + article.getProject().getName());
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à un risque de perte" +
                            " dans l'article " + article.getRessources() + " puisque on a prédicté que il a besoin de " + ((Number) map.get("prediction")).floatValue() + " Jours" +
                            " mais on a vendu que " + article.getJ_Vendus() + " Jours.");
                    notification.setProfitability(NotificationDegree.Warning);
                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);
                }


            } else {
                if ((article.getAttérissage() > article.getJ_Vendus()) && (article.getConsommés_J() < article.getJ_Vendus())) {
                    Notification notification = article.getNotification();
                    if (article.getNotification() == null) {
                        notification = new Notification();
                    }

                    notification.setTitle("Risque de perte");
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à un risque de perte" +
                            " dans l'article " + article.getRessources() + " puisque il a demandé " + article.getAttérissage() + " Jours" +
                            " mais on a vendu que " + article.getJ_Vendus() + " Jours.");
                    notification.setProductivity(NotificationDegree.Warning);
                    notification.setArticle(article);
                    article.setNotification(notification);

                    articleRepository.save(article);
                }
                if (article.getConsommés_J() > article.getJ_Vendus()) {
                    Notification notification = article.getNotification();
                    if (article.getNotification() == null) {
                        notification = new Notification();
                        if (article.getMarge_en_montant() >= 0) {
                            notification.setTitle("Une perte du marge");
                            notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une perte" +
                                    " dans l'article " + article.getRessources() + "puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n" + "La perte financiére maintenant est" + article.getJ_Restant() * article.getCouts()
                                    + " DT Le pourcentage du marge est devenir  "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.");
                            notification.setProfitability(NotificationDegree.Warning);

                        } else {
                            notification.setTitle("Une perte de " + article.getJ_Restant() * article.getCouts() + " DT");
                            notification.setDescription("Une perte de " + article.getMarge_en_montant() +
                                    " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n  Le projet a une pourcentage de perte de "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.\n");
                            notification.setProfitability(NotificationDegree.Dangerous);
                        }
                    } else {
                        if (article.getMarge_en_montant() >= 0) {
                            notification.setTitle(notification.getTitle() + "et une perte du marge");
                            notification.setDescription(notification.getDescription() + "et  une perte du marge" +
                                    article.getCouts() * article.getJ_Restant() + " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le pourcentage du marge est devenir  "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.");

                            notification.setProfitability(NotificationDegree.Warning);

                        } else {
                            notification.setTitle(notification.getTitle() + "et une perte de " + article.getJ_Restant() * article.getCouts() + " DT");
                            notification.setDescription("Une perte de " + article.getMarge_en_montant() +
                                    " DT puisque on a vendu" + article.getJ_Vendus() + " Jours" +
                                    "mais on a maintenant dépassé  " + article.getConsommés_J() + " Jours.\n Le projet a une pourcentage de perte de "
                                    + article.getMarge_en_montant() / article.getCouts() * 100 + " %.\n");
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
    public void checkPerformance() {
        List<Article> articles = articleService.findByStatus(ArticleStatus.COMPLETED);

        for (Article article : articles) {
            //  if(!article.isRead()){
            Notification notification = new Notification();
            notification.setArticle(article);
            article.setNotification(notification);
            if (article.getAttérissage().equals(article.getJ_Vendus())) {
                notification.setTitle("Productivité equilibré");
                notification.setDescription("L'article " + article.getRessources() + " du " + article.getProject().getName() + " a une productivité equilibré avec des jours" +
                        " consommés equivaut aux jours vendus de " + article.getJ_Vendus() + " Jours.");
                notification.setProductivity(NotificationDegree.Successful);


            }
            if (article.getAttérissage() > article.getJ_Vendus()) {
                notification.setTitle("Productivité faible");
                notification.setDescription("L'article " + article.getRessources() + " du " + article.getProject().getName() + " a une productivité faible avec des jours" +
                        " perdus  de " + article.getJ_Restant() + " Jours.");
                notification.setProductivity(NotificationDegree.Dangerous);

            }
            if (article.getAttérissage() < article.getJ_Vendus()) {
                notification.setTitle("Productivité excellente");
                notification.setDescription("L'article " + article.getRessources() + " du " + article.getProject().getName() + " a une productivité éxcéllente avec des jours" +
                        " additionnels  de " + article.getJ_Restant() + " Jours.");
                notification.setProductivity(NotificationDegree.Successful);
            }
            articleRepository.save(article);
        }

        //   }
    }

    @Override
    public void checkRentability(float percentage) {
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
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public List<Map<String, Object>> findForProjectInProgress() {
        List<Map<String, Object>> notifications = new ArrayList<>();
        List<Notification> all = notificationRepository.findAll();
        for (Notification notification : all) {
            if (notification.getArticle().getStatus().equals(ArticleStatus.IN_PROGRESS)) {
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("id", notification.getId());
                notificationData.put("title", notification.getTitle());
                notificationData.put("description", notification.getDescription());
                notificationData.put("productivity", notification.getProductivity());
                notificationData.put("profitability", notification.getProfitability());
                notificationData.put("projectName", notification.getArticle().getProject().getName());
                notificationData.put("articleName", notification.getArticle().getRessources());
                notifications.add(notificationData);
            }
        }

        return notifications;
    }

    @Override
    public List<Map<String, Object>> findForProjectCompleted() {
        List<Map<String, Object>> notifications = new ArrayList<>();
        List<Notification> all = notificationRepository.findAll();
        for (Notification notification : all) {
            if (notification.getArticle().getStatus().equals(ArticleStatus.COMPLETED)) {
                Map<String, Object> notificationData = new HashMap<>();
                notificationData.put("id", notification.getId());
                notificationData.put("title", notification.getTitle());
                notificationData.put("description", notification.getDescription());
                notificationData.put("productivity", notification.getProductivity());
                notificationData.put("profitability", notification.getProfitability());
                notificationData.put("projectName", notification.getArticle().getProject().getName());
                notificationData.put("articleName", notification.getArticle().getRessources());
                notifications.add(notificationData);
            }
        }

        return notifications;
    }

    public void predictionCheck() {
        List<Article> articles = articleService.findByStatus(ArticleStatus.IN_PROGRESS);

        for (Article article : articles) {
            //  if(!article.isRead()){
            Notification notification = new Notification();
            notification.setArticle(article);
            article.setNotification(notification);
            if (article.getAttérissage().equals(article.getJ_Vendus())) {
                notification.setTitle("Productivité equilibré");
                notification.setDescription("L'article " + article.getRessources() + " du " + article.getProject().getName() + " a une productivité equilibré avec des jours" +
                        " consommés equivaut aux jours vendus de " + article.getJ_Vendus() + " Jours.");
                notification.setProductivity(NotificationDegree.Successful);


            }
            if ((article.getAttérissage() > article.getJ_Vendus()) && (article.getConsommés_J() < article.getJ_Vendus())) {
                notification.setTitle("Risque de perte");
                notification.setDescription("L'article " + article.getRessources() + " du " + article.getProject().getName() + " a une productivité faible avec des jours" +
                        " perdus  de " + article.getJ_Restant() + " Jours.");
                notification.setProductivity(NotificationDegree.Dangerous);

            }
            if (article.getAttérissage() < article.getJ_Vendus()) {
                notification.setTitle("Productivité excellente");
                notification.setDescription("L'article " + article.getRessources() + " du " + article.getProject().getName() + " a une productivité éxcéllente avec des jours" +
                        " additionnels  de " + article.getJ_Restant() + " Jours.");
                notification.setProductivity(NotificationDegree.Successful);
            }
            articleRepository.save(article);
        }

    }

    @Override
    public Map<String, Object> getPerformanceCompleted() {
        Map<String, Object> performanceCompleted = new HashMap<>();
        Map<String, Object> productivity = new HashMap<>();
        Map<String, Object> profitability = new HashMap<>();
        List<Article> articles = articleService.findByStatus(ArticleStatus.COMPLETED);
        Integer prod_dang = 0;
        Integer prod_suc = 0;
        Integer prod_normal = 0;
        Integer prof_dang = 0;
        Integer prof_suc = 0;
        Integer prof_normal = 0;
        for (Article article : articles) {
            Notification notification = article.getNotification();
            if (notification.getProductivity().equals(NotificationDegree.Dangerous)) {
                prod_dang++;
            }
            if (notification.getProductivity().equals(NotificationDegree.Successful)) {
                prod_suc++;
            }

            if (notification.getProductivity().equals(NotificationDegree.Normal)) {
                prod_normal++;
            }

            if (notification.getProfitability().equals(NotificationDegree.Dangerous)) {
                prof_dang++;
            }
            if (notification.getProfitability().equals(NotificationDegree.Successful)) {
                prof_suc++;
            }

            if (notification.getProfitability().equals(NotificationDegree.Normal)) {
                prof_normal++;
            }
        }
        productivity.put("Mauvaise", prod_dang);
        productivity.put("Excellente", prod_suc);
        productivity.put("Normal", prod_normal);

        profitability.put("Excellente", prof_suc);
        profitability.put("Normal", prof_normal);
        profitability.put("Mauvaise", prof_dang);
        performanceCompleted.put("Rentabilité", profitability);
        performanceCompleted.put("Productivité", productivity);
        return performanceCompleted;
    }

    public void articleScanProfitability(Integer id, float marge) {
        Article article = articleRepository.findById(id).orElse(null);
        float percentage_win = ((article.getBudget() - article.getCouts()) / article.getCouts()) * 100;
        Notification notification = article.getNotification();
    //    if (article.getStatus().equals(ArticleStatus.COMPLETED)){
        if (percentage_win >= marge) {
            notification.setProfitability(NotificationDegree.Successful);
            notification.setTitle(notification.getTitle() + " et Article profitable");
            notification.setDescription(notification.getDescription() + " avec une profitabilité excellente de " + percentage_win + " %.");
        }
        if ((percentage_win < marge) && (percentage_win > 0)) {
            notification.setTitle(notification.getTitle() + " et Rentabilité faible");
            notification.setDescription(notification.getDescription() + " avec une rentabilité Insuffisante de " + percentage_win + " %.");
            notification.setProfitability(NotificationDegree.Normal);
        }
        if (percentage_win <= 0) {
            notification.setTitle(notification.getTitle() + " et Une perte financiére");
            notification.setDescription(notification.getDescription() + " avec une rentabilité gravement réalisée par une perte de " + percentage_win + " %.");
            notification.setProfitability(NotificationDegree.Dangerous);

        }
   // }
        article.setSeen(true);
        article.setNotification(notification);

        articleRepository.save(article);
    }

    public void articleScanProductivity(Integer id) {
        Article article = articleRepository.findById(id).orElse(null);
        Notification notification = new Notification();
        notification.setArticle(article);
        float ratio=0f;
        if (article.getAttérissage()==0){
            ratio=0;
        }
        else{ ratio = article.getJ_Vendus() / article.getAttérissage();}

        if (ratio == 1f) {
            notification.setTitle("Productivité stable");
            notification.setDescription("Une productivité stable avec " + article.getJ_Vendus() + "  jours vendus, correspondant exactement aux jours nécessaires.");
            notification.setProductivity(NotificationDegree.Normal);
        } else if (ratio < 1) {
            notification.setTitle( "Productivité faible");
            notification.setDescription("Une productivité faible avec " + article.getBudget_Additionnel() + " jours de moins, car on a vendu que " + article.getJ_Vendus() + " jours ce qui nécessitait " + article.getAttérissage() + " jours.");
            notification.setProductivity(NotificationDegree.Dangerous);
        } else if (ratio > 1) {
            notification.setTitle("Productivité excellente");
            notification.setDescription("Une productivité excellente avec " + article.getBudget_Additionnel() + " jours additionnels, car on a vendu que " + article.getJ_Vendus() + " jours ce qui nécessitait seulement " + article.getAttérissage() + " jours.");
            notification.setProductivity(NotificationDegree.Successful);
        }
        article.setSeen(true);
        notification.setStatus(StatusNotif.COMPLETED);
        article.setNotification(notification);
        articleRepository.save(article);
    }

    public void articleScanPrediction(Integer id) {
        Article article = articleRepository.findById(id).orElse(null);
        Notification notification = new Notification();
        Project project = article.getProject();
        if ((!article.getRessources().equals("Gestion et coordination du projet")) && (!article.getRessources().equals("Analyste concepteur"))&&(article.getProject().getType().equals("DRUPAL"))) {
            Map<String, Object> map = articleService.predict(id);
            if ((article.getJ_Vendus() < ((Number) map.get("prediction")).floatValue()) && (article.getConsommés_J() < article.getJ_Vendus())) {
                Map<String, Object> cost = flaskServices.predictCost("[{ Ressources:"+article.getRessources()+", Coût unitaire: "+article.getCoût_unitaire()+", J/H Vendus: "+article.getJ_Vendus()+"}]");
                notification.setArticle(article);
                notification.setTitle(notification.getTitle()+" Risque de perte ");
                notification.setDescription(notification.getDescription()+" \n mais avec un risque de perte car nous avions prévu qu'il en faudrait " + ((Number) map.get("prediction")).floatValue() + " jours" +
                        " alors que nous n'en avons vendu que " + article.getJ_Vendus() + ", ce qui va nous coûter "+((Number) cost.get("prediction")).floatValue()+" DT au lieu de "+article.getCouts()+" DT.");
            }



            articleRepository.save(article);
        }else {


            if((article.getJ_Vendus()<article.getAttérissage())&&(article.getConsommés_J()<=article.getJ_Vendus())){
                notification.setTitle("Risque de perte");
             notification.setDescription("Si nous continuons avec cette stratégie, nous perdrons "+article.getMarge()+" jours ");
             notification.setProductivity(NotificationDegree.Warning);
             notification.setArticle(article);
            }
            if(article.getConsommés_J()>article.getJ_Vendus()){
                notification.setTitle("Productivité mauvaise");
                notification.setDescription("Nous avons déjà dépassé les jours vendus, par conséquent, nous avons maintenant perdu "+article.getMarge()+" jours ");
                notification.setProductivity(NotificationDegree.Dangerous);
                notification.setArticle(article);
            }
            article.setNotification(notification);
            articleRepository.save(article);

        }

    }
    public void articleScanPredictionProfitability(Integer id, float win) {
        // Fetch the article from the repository
        Article article = articleRepository.findById(id).orElse(null);

        // Check if the article exists and has the required properties
        if (article == null || article.getProject() == null) {
            throw new IllegalArgumentException("Article or project not found");
        }

        // Check if the article's resource and project type meet the criteria
        if (!article.getRessources().equals("Gestion et coordination du projet") &&
                !article.getRessources().equals("Analyste concepteur") &&
                article.getProject().getType().equals("DRUPAL")) {

            // Format the request data as a JSON string
            String jsonString = String.format(
                    "[{ \"Ressources\":\"%s\", \"Coût unitaire\": %f, \"J/H Vendus\": %f }]",
                    article.getRessources(),
                    article.getCoût_unitaire(),
                    article.getJ_Vendus()
            );

            // Get the cost prediction from Flask service
            Map<String, Object> cost = flaskServices.predictCost(jsonString);

            // Extract predictions list and calculate the percentage win
            List<?> predictions = (List<?>) cost.get("predictions");
            if (predictions.isEmpty()) {
                throw new RuntimeException("No predictions returned from Flask service");
            }

            // Get the first prediction
            float prediction = ((Number) predictions.get(0)).floatValue();

            // Calculate the percentage win
            float percentageWin = ((article.getBudget() - prediction) / prediction) * 100;

            // Create and configure a notification
            Notification notification = new Notification();
            if (article.getCouts() < prediction) {
                notification.setTitle("Coûts Plus Élevés que Prévu");
                notification.setDescription("Les coûts réels d'article se sont élevé à " + article.getCouts() + " DT, comparés à une prévision de " + prediction + " DT. \n " +
                        "Ce qui va vous donner une profitabilité de " + percentageWin + " % de valeur " + (article.getBudget()- prediction) + " DT");
                notification.setProductivity(NotificationDegree.Warning);
            } else {
                notification.setTitle("Coûts Stable");
                notification.setDescription("Les coûts réels d'article se sont stables à " + article.getCouts() + " DT \n " +
                        "Ce qui va vous donner une profitabilité de " + percentageWin + " % de valeur " + article.getMarge_en_montant() + " DT");
                notification.setProductivity(NotificationDegree.Normal);
            }

            // Set profitability degree based on percentageWin
            if (percentageWin < win) {
                notification.setProfitability(NotificationDegree.Dangerous);
            } else if (percentageWin == win) {
                notification.setProfitability(NotificationDegree.Normal);
            } else {
                notification.setProfitability(NotificationDegree.Successful);
            }

            notification.setStatus(StatusNotif.NOT_YET);
        article.setNotification(notification);

        articleRepository.save(article);
    } }
//this function is to create all the notif for all articles
    @Override
    public void articleScan(){
        List<Article> articles = articleService.findByStatus(ArticleStatus.COMPLETED);
        List<Article> articlesP = articleService.findByStatus(ArticleStatus.IN_PROGRESS);
        for (Article article : articles) {
            articleScanProductivity(article.getId());
            articleScanProfitability(article.getId(),20f);
        }
        for (Article article : articlesP) {
            articleScanPredictionProfitability(article.getId(),20f);
          //  articleScanProfitability(article.getId(),20f);
        }
    }
    @Override
    public List<Notification> FindByStatus(StatusNotif status){
      return   notificationRepository.findByStatus(status);
    }

    @Override
    public Map<String,List<Notification>> findDecisions(){
        List<Notification> perte=notificationRepository.findByProfitabilityAndAndProductivity(NotificationDegree.Successful,NotificationDegree.Warning);
        List<Notification> block=notificationRepository.findByProfitability(NotificationDegree.Dangerous);
        Map<String,List<Notification>> map = new HashMap<>();
        map.put("perte",perte);
        map.put("block",block);
        return map;
    }

}
