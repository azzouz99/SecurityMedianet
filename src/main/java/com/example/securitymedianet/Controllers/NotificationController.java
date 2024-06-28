package com.example.securitymedianet.Controllers;


import com.example.securitymedianet.Services.Notification.NotificationServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notif")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationServices services;

    @PostMapping("/check")
  public   void Notificationcheck(){
        services.checkNotifications();
    }
    @DeleteMapping("/delete/{id}")
   public void deleteNotification(@PathVariable("id") Long id){
      services.deleteNotification(id);
   }
}
