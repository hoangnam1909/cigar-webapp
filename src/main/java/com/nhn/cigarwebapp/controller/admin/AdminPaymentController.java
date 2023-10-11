package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.repository.OrderRepository;
import com.nhn.cigarwebapp.repository.PaymentRepository;
import com.nhn.cigarwebapp.service.MomoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin/payments")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPaymentController {

    @Value("${payment.momo.prefix-orderid}")
    public String prefixOrderId;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MomoService momoService;

    @PatchMapping("/update-payment-status/{orderId}")
    public ResponseEntity<ResponseObject> updatePaymentStatus(@PathVariable Long orderId) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();

                Payment payment = order.getPayment();
                payment.setIsPaid(momoService.checkTransactionStatus(order));
                paymentRepository.save(payment);

                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Payment result updated")
                                .result(null)
                                .build());
            } else {
                throw new IllegalArgumentException("Could not found order with id " + orderId);
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result(ex.getMessage())
                            .build());
        }
    }

}
