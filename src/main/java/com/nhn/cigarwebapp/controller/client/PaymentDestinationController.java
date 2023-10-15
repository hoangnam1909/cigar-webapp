package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.payment.PaymentDestinationResponse;
import com.nhn.cigarwebapp.service.PaymentDestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-destinations")
public class PaymentDestinationController {

    private final PaymentDestinationService paymentDestinationService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts() {
        List<PaymentDestinationResponse> responses = paymentDestinationService.getPaymentDestinations();
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Payment destinations found")
                        .result(responses)
                        .build());
    }

}
