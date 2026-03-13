package org.example.onlinevotingsystem.repositories;


import org.example.onlinevotingsystem.models.Notification;
import org.example.onlinevotingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient(User voter);
}