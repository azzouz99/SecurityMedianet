package com.example.securitymedianet.Repositories;

import com.example.securitymedianet.Entites.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification, Long> {

}
