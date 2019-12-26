package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcNotificationRepository extends JpaRepository<NotificationEntity, Integer> {
}
