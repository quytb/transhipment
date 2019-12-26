package com.havaz.transport.dao.repository;

import com.havaz.transport.dao.entity.TcNotificationConfigureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TcNotificationConfigRepository extends JpaRepository<TcNotificationConfigureEntity, Integer> {
    List<TcNotificationConfigureEntity> findByNotificationId(Integer notificationId);
}
