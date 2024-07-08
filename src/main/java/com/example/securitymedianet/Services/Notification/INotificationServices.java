package com.example.securitymedianet.Services.Notification;

import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.Notification;

import java.util.List;
import java.util.Map;

public interface INotificationServices {
    Article getArticle(Long notificationId);

    void checkInprogress();

    void checkPerformance();

    void checkRentability(float percentage);

    void deleteNotification(Long id);

    List<Map<String, Object>> findForProjectInProgress();

    List<Map<String, Object>> findForProjectCompleted();

    Map<String,Object> getPerformanceCompleted();
}
