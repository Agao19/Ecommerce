//package com.ZhongHou.Ecommerce.dto.Payment.configWS;
//
//import com.ZhongHou.Ecommerce.dto.Payment.IpnHandler;
//import com.ZhongHou.Ecommerce.repository.OrderRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class VnPayAsyncService {
//    private final IpnHandler ipnHandler;
//    private final OrderRepository orderRepository;
//    private final SimpMessagingTemplate messagingTemplate;
//
//
//    @Async
//    public void processPayment(Map<String, String> params){
//        try {
//            Thread.sleep(10000);
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        String orderId = params.get("vnp_TxnRef");
//        String responseCode = params.get("vnp_ResponseCode");
//
//        // 3. BẮN WEBSOCKET ĐỂ BÁO CHO FE
//        String status = "00".equals(responseCode) ? "SUCCESS" : "FAILED";
//
//
//        // Tạo 1 DTO đơn giản để gửi đi
//        Map<String, String> wsMessage = Map.of(
//                "status", status,
//                "message", "Thanh toán đã được xử lý"
//        );
//
//
//        // Gửi tin nhắn đến topic mà FE đang lắng nghe
//        // (Lấy orderId làm topic)
//        messagingTemplate.convertAndSend("/topic/payment-status/" + orderId, wsMessage);
//    }
//}
