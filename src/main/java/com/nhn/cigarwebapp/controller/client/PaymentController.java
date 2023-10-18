package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @PatchMapping("/update-payment-status")
    public ResponseEntity<ResponseObject> updatePaymentStatus(@RequestBody Map<String, String> params) {
//        try {
            long orderId;
            if (params.containsKey("orderId"))
                orderId = Long.parseLong(params.get("orderId").split("-")[1].substring(3));
            else if (params.containsKey("vnp_TxnRef"))
                orderId = Long.parseLong(params.get("vnp_TxnRef").split("-")[1].substring(3));
            else
                orderId = -1;

            boolean isPaid = paymentService.updatePaymentStatus(orderId, params);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Updated paid status")
                            .result(isPaid)
                            .build());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            logger.error(ex.getMessage());
//            return ResponseEntity.internalServerError()
//                    .body(ResponseObject.builder()
//                            .msg("Error!")
//                            .result("Internal Server Error")
//                            .build());
//        }
    }

//    @GetMapping("/confirm-payment")
//    public ResponseEntity<?> confirmPayment(@RequestParam Map<String, String> params) {
//        try {
//            boolean paymentResult = momoService.confirmPayment(params);
//
//            if (paymentResult) {
//                return ResponseEntity.ok()
//                        .body(ResponseObject.builder()
//                                .msg("Paid")
//                                .result(true)
//                                .build());
//            } else {
//                return ResponseEntity.ok()
//                        .body(ResponseObject.builder()
//                                .msg("Non-Payment")
//                                .result(false)
//                                .build());
//            }
//        } catch (Exception ex) {
//            return ResponseEntity.badRequest()
//                    .body(ResponseObject.builder()
//                            .msg("We could not save your order")
//                            .result(ex.getMessage())
//                            .build());
//        }
//
//        return ResponseEntity.notFound().build();
//    }

}
