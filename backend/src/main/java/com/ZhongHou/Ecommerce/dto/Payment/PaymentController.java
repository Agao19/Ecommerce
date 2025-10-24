package com.ZhongHou.Ecommerce.dto.Payment;
import com.ZhongHou.Ecommerce.dto.Payment.constant.IpnResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    //private final VnPayAsyncService vnPayAsyncService;
   // private final PaymentService paymentService;

    private final StripeService stripeService;
    private final IpnHandler ipnHandler;

    @PostMapping("/pay")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(stripeService.createPaymentIntent(paymentRequest));
    }


    @PostMapping("/vnpay_ipn") // nhận từ VNPAY 
    IpnResponse processIpn(@RequestParam Map<String, String> params) throws UnsupportedEncodingException {
        return ipnHandler.process(params);
    }


    @GetMapping("/vnpay_return")
    public void processIpntestReturnUrl(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        IpnResponse res = ipnHandler.process(params);
        if (res.getResponseCode().equals("00")) {
            // Redirect về Frontend với thông tin thành công
            String frontendUrl = "https://sandbox.vnpayment.vn/apis/vnpay-demo";
            response.sendRedirect(frontendUrl);
        } else {
            String frontendUrl = "abc";
            response.sendRedirect(frontendUrl);
        }
    }



//
//    //test return url with WEBSOCKET
//    @GetMapping("/vnpay_return")
//    public RedirectView processIpntestReturnUrl(@RequestParam Map<String, String> params) throws IOException {
//
//        String vnp_ResponseCode = params.get("vnp_ResponseCode");
//        String vnp_TxnRef = params.get("vnp_TxnRef");
//        vnPayAsyncService.processPayment(params);
//        String frontendUrl = "http://localhost:4200/payments";
//        String redirectUrlWithParams = String.format("%s?orderId=%s&responseCode=%s",
//                frontendUrl, vnp_TxnRef, vnp_ResponseCode);
//
//        return new RedirectView(redirectUrlWithParams);
//    }


//     @PostMapping("/stripe")
//     public ResponseEntity<?> createPaymentStripeIntent(@RequestBody PaymentRequest paymentRequest) throws Exception {
//         return ResponseEntity.ok(paymentService.initiate(paymentRequest));
//     }
//
//    @PostMapping("/momo")
//    public ResponseEntity<?> createPaymentMomoIntent(@RequestBody PaymentRequest paymentRequest) throws Exception {
//        return ResponseEntity.ok(paymentService.initiate(paymentRequest));
//    }


}
