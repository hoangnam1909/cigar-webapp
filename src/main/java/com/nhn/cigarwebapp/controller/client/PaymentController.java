package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.repository.PaymentRepository;
import com.nhn.cigarwebapp.service.MomoService;
import com.nhn.cigarwebapp.specification.payment.PaymentEnum;
import com.nhn.cigarwebapp.specification.payment.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final MomoService momoService;
    private final PaymentRepository paymentRepository;

    @PatchMapping("/update-payment-status")
    public ResponseEntity<ResponseObject> updatePaymentStatus(@RequestBody Map<String, String> params) {
        try {
            PaymentSpecification specification = new PaymentSpecification();
            specification.add(new SearchCriteria(PaymentEnum.PAYMENT_ORDER_ID, params.get(PaymentEnum.PAYMENT_ORDER_ID), SearchOperation.EQUAL));
            specification.add(new SearchCriteria(PaymentEnum.REQUEST_ID, params.get(PaymentEnum.REQUEST_ID), SearchOperation.EQUAL));

            List<Payment> payments = paymentRepository.findAll(specification);

            if (payments.size() == 1) {
                System.err.println("is present");
                Payment payment = payments.get(0);
                boolean isPaid = momoService.checkTransactionStatus(params);
                payment.setIsPaid(isPaid);
                paymentRepository.save(payment);

                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Updated paid status")
                                .result(isPaid)
                                .build());
            } else {
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Invalid data")
                                .result(null)
                                .build());
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not save your order")
                            .result(ex.getMessage())
                            .build());
        }
    }

    @GetMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestParam Map<String, String> params) {
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

        return ResponseEntity.notFound().build();
    }

}
