package com.ZhongHou.Ecommerce.service.impl;

import com.ZhongHou.Ecommerce.dto.NotificationDTO;
import com.ZhongHou.Ecommerce.entity.Notification;
import com.ZhongHou.Ecommerce.enums.NotficationType;
import com.ZhongHou.Ecommerce.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotiServiceImpl implements NotiService{

    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;
    @Override
    public void sendEmail(NotificationDTO notificationDTO) {
        log.info("INSIDE SENDING EMAIL.....");

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notificationDTO.getRecipient());
        simpleMailMessage.setSubject(notificationDTO.getSubject());
        simpleMailMessage.setText(notificationDTO.getBody());

        javaMailSender.send(simpleMailMessage);

        //Save db
        Notification notificationToSave =Notification.builder()
                .recipient(notificationDTO.getRecipient())
                .subject(notificationDTO.getSubject())
                .body(notificationDTO.getBody())
                .orderReference(notificationDTO.getOrderReference())
                .type(NotficationType.EMAIL)
                .build();

        notificationRepository.save(notificationToSave);

    }
}
