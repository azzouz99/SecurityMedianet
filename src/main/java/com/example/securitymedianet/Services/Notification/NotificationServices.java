package com.example.securitymedianet.Services.Notification;

import com.example.securitymedianet.Entites.*;
import com.example.securitymedianet.Repositories.NotificationRepository;
import com.example.securitymedianet.Services.Article.ArticleService;
import com.example.securitymedianet.Services.Flask.FlaskServices;
import com.example.securitymedianet.Services.Project.ProjectServices;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationServices implements INotificationServices {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ProjectServices projectServices;

    @Autowired
    ArticleService articleService;
    @Autowired
    private FlaskServices flaskServices;
@Override
    public void checkNotifications() {
        List<Article> articles = articleService.findByStatus(ArticleStatus.IN_PROGRESS);

        for (Article article : articles) {
            Project project = article.getProject();
            if ((project.getType().equals("DRUPAL"))&&(!article.getRessources().equals("Gestion et coordination du projet"))&&(!article.getRessources().equals("Analyste concepteur"))) {
            Map<String,Object> map=articleService.predict(article.getId());



                if (article.getAttérissage() > ((Number) map.get("prediction")).floatValue()) {
                    Notification notification = new Notification();
                    notification.setTitle("Mauvaise Planification");
                    notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une mauvaise planification" +
                            " dans l'article " + article.getRessources() + "puisque on a prédicté que il a besoin de" + ((Number) map.get("prediction")).floatValue() + " Jours" +
                            "mais on a mettre " + article.getAttérissage()+" Jours");
                    notification.setDegree(NotificationDegree.Warning);
                    notificationRepository.save(notification);


                }
                if (article.getConsommés_J() > article.getJ_Vendus()) {
                    Notification notification = new Notification();
                    notification.setTitle("Une perte");
                    notification.setDescription("Votre " + project.getName() + "de type " + project.getType() + " à une perte" +
                            " dans l'article " + article.getRessources() + "puisque on a vendu" + article.getJ_Vendus()+ " Jours" +
                            "mais on a maintenant dépassé  " + article.getConsommés_J()+" Jours.\n" +"La perte financiére maintenant est"+article.getJ_Restant()*article.getCouts());
                    notification.setDegree(NotificationDegree.Warning);
                    notificationRepository.save(notification);

                }
                if (article.getJ_Vendus() < ((Number) map.get("prediction")).floatValue()) {
                    Notification notification = new Notification();
                    notification.setTitle("Risque de perte");
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à un risque de perte" +
                            " dans l'article " + article.getRessources() + " puisque on a prédicté que il a besoin de " + ((Number) map.get("prediction")).floatValue() + " Jours" +
                            " mais on a vendu que " + article.getJ_Vendus()+" Jours.");
                    notification.setDegree(NotificationDegree.Warning);
                    notificationRepository.save(notification);


                }
            } else {
                if (article.getAttérissage() > article.getJ_Vendus()) {
                    Notification notification = new Notification();
                    notification.setTitle("Risque de perte");
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à un risque de perte" +
                            " dans l'article " + article.getRessources() + "puisque il a demandé " + article.getAttérissage() + " Jours" +
                            " mais on a vendu que " + article.getJ_Vendus()+" Jours.");
                    notification.setDegree(NotificationDegree.Warning);
                    notificationRepository.save(notification);
                }
                if (article.getConsommés_J() > article.getJ_Vendus()) {
                    Notification notification = new Notification();
                    notification.setTitle("Une perte");
                    notification.setDescription("Votre " + project.getName() + " de type " + project.getType() + " à une perte" +
                            " dans l'article " + article.getRessources() + " puisque on a vendu" + article.getJ_Vendus()+ " Jours" +
                            " mais on a maintenant dépassé  " + article.getConsommés_J()+" Jours.\n" +"La perte financiére maintenant est "+article.getJ_Restant()*article.getCouts());
                    notification.setDegree(NotificationDegree.Warning);
                    notificationRepository.save(notification);

                }

            }
        }
    }
    @Override
    public void deleteNotification(Long id){
        notificationRepository.deleteById(id);
    }
}
