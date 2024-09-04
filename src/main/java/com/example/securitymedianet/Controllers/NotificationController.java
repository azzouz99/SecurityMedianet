package com.example.securitymedianet.Controllers;


import com.example.securitymedianet.Entites.Article;
import com.example.securitymedianet.Entites.Notification;
import com.example.securitymedianet.Entites.StatusNotif;
import com.example.securitymedianet.Services.Notification.INotificationServices;
import com.example.securitymedianet.Services.Notification.NotificationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notif")
@RequiredArgsConstructor
public class NotificationController {
    private final INotificationServices services;

    @GetMapping("/status/{status}")
    public List<Notification> getNotificationByStatus(@PathVariable("status") StatusNotif status) {
        return services.FindByStatus(status);
    }
@GetMapping("/article/{id}")
public Article getArticle(@PathVariable Long id) {
    return services.getArticle(id);
}
    @PostMapping("/inprogress/check")
   public void Inprogresscheck(){
        services.checkInprogress();
    }
    @DeleteMapping("/delete/{id}")
   public void deleteNotification(@PathVariable("id") Long id){
      services.deleteNotification(id);
   }

    @PostMapping("/completed/productivity")
    public void CheckProductivity(){
        services.checkPerformance();
    }
    @PostMapping("/completed/profitability/{percentage}")
    public void Checkprofitability(@PathVariable("percentage") float percentage){
        services.checkRentability(percentage);
    }
     @GetMapping("/completed")
    public List<Map<String, Object>> findForProjectCompleted(){
        return services.findForProjectCompleted();
    }
    @GetMapping("/inprogress")
    public  List<Map<String, Object>> findForProjectInProgress(){
        return services.findForProjectInProgress();
    }

    @GetMapping("/performance/completed")
    public Map<String, Object> getPerformanceCompleted(){
    return services.getPerformanceCompleted();
    }

    @GetMapping("/decisions")
    public Map<String, List<Notification>> findForDecisions(){
        return services.findDecisions();
    }
}
