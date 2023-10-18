package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(AdminPaymentController.class);

    private final PaymentService paymentService;

    @PatchMapping("/update-payment-status/{orderId}")
    public ResponseEntity<ResponseObject> updatePaymentStatus(@PathVariable Long orderId) {
        boolean isPaid = paymentService.updatePaymentStatus(orderId);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Payment status updated!")
                        .result(isPaid)
                        .build());
    }

    @PatchMapping("/recreate-payment-url/{orderId}")
    public ResponseEntity<ResponseObject> recreatePaymentUrl(@PathVariable Long orderId) {
        try {
            paymentService.recreatePaymentUrl(orderId);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Recreate payment url successfully!")
                            .result(null)
                            .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

}
