package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.NotificationDTO;

public interface NotiService {
    void sendEmail(NotificationDTO notificationDTO);
}
