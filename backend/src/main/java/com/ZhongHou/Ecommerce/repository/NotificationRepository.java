package com.ZhongHou.Ecommerce.repository;

import com.ZhongHou.Ecommerce.dto.NotificationDTO;
import com.ZhongHou.Ecommerce.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
