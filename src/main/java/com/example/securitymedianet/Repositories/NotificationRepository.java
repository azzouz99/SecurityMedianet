package com.example.securitymedianet.Repositories;

import com.example.securitymedianet.Entites.Notification;
import com.example.securitymedianet.Entites.NotificationDegree;
import com.example.securitymedianet.Entites.StatusNotif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification, Long> {
List <Notification> findByStatus(StatusNotif status);
List<Notification> findByProductivity(NotificationDegree productivity);
List<Notification> findByProfitability(NotificationDegree profitability);
List<Notification> findByProfitabilityAndAndProductivity(NotificationDegree profitability, NotificationDegree productivity);
}
