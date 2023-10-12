package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/payments")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPaymentController {

    private final PaymentService paymentService;

    @PatchMapping("/update-payment-status/{orderId}")
    public ResponseEntity<ResponseObject> updatePaymentStatus(@PathVariable Long orderId) {
        try {
            paymentService.updatePaymentStatus(orderId);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Payment status updated!")
                            .result(null)
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result(ex.getMessage())
                            .build());
        }
    }

}
