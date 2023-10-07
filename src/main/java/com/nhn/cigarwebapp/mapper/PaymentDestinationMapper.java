package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.payment.PaymentDestinationResponse;
import com.nhn.cigarwebapp.entity.PaymentDestination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentDestinationMapper {

    public PaymentDestinationResponse toResponse(PaymentDestination paymentDestination){
        return PaymentDestinationResponse.builder()
                .id(paymentDestination.getId())
                .logo(paymentDestination.getLogo())
                .shortName(paymentDestination.getShortName())
                .name(paymentDestination.getName())
                .build();
    }

}
