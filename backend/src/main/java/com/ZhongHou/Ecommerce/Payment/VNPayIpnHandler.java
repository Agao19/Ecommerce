package com.ZhongHou.Ecommerce.Payment;

import com.ZhongHou.Ecommerce.Payment.constant.IpnResponse;
import com.ZhongHou.Ecommerce.Payment.constant.VNPayParams;
import com.ZhongHou.Ecommerce.Payment.constant.VnpIpnResponseConst;
import com.ZhongHou.Ecommerce.entity.Order;
import com.ZhongHou.Ecommerce.enums.PaymentStatus;
import com.ZhongHou.Ecommerce.exception.BusinessException;
import com.ZhongHou.Ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayIpnHandler implements IpnHandler {

    private final VNPayService vnPayService;
    private final OrderRepository orderRepository;


    @Override
    public IpnResponse process(Map<String, String> params) throws UnsupportedEncodingException {
        if (!vnPayService.verifyIpn(params)) {
            log.info("VNPay IPN Signature Failed.");
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }

        try {
            var txnRef=params.get(VNPayParams.TXN_REF);
            var responseCode = params.get(VNPayParams.RESPONSE_CODE);
            var transactionStatus = params.get(VNPayParams.TRANSACTION_NO);
            var bankCode = params.get(VNPayParams.BANK_CODE);
            Optional<Order> orderIdOptional  = orderRepository.findByOrderReference(txnRef);

            if (orderIdOptional.isEmpty()) {
                log.info("VNPay IPN: Order not found for TxnRef: {}", txnRef);
                return VnpIpnResponseConst.ORDER_NOT_FOUND;
            }

            log.info("VNPay IPN: Successfully processed TxnRef: {}", txnRef);
            Order orderId=orderIdOptional.get();
            if(!"00".equals(responseCode)) {
                orderId.setPaymentStatus(PaymentStatus.FAILED);
                log.info("VNPay IPN: Payment successful for TxnRef: {}, BankCode: {}",
                    txnRef, bankCode);
            }else{
                orderId.setPaymentStatus(PaymentStatus.COMPLETED);
            }
            orderRepository.save(orderId);
            return VnpIpnResponseConst.SUCCESS;


        } catch (BusinessException e) {
            log.info("Unhandled Exception processing IPN for TxnRef {}: {}",
                    params.get(VNPayParams.TXN_REF), e.getMessage(), e);
            return VnpIpnResponseConst.UNKNOWN_ERROR;
        }
    }


}
