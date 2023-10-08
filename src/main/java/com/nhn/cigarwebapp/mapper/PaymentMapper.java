package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.payment.PaymentResponse;
import com.nhn.cigarwebapp.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentMapper {

    private final PaymentDestinationMapper destinationMapper;

    public PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .createdDate(payment.getCreatedDate())
                .paidAmount(payment.getPaidAmount())
                .isPaid(payment.getIsPaid())
                .paymentUrl(payment.getPaymentUrl())
                .paymentDestination(destinationMapper.toResponse(payment.getPaymentDestination()))
                .build();
    }

}
