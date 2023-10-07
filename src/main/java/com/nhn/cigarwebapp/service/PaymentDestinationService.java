package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.response.payment.PaymentDestinationResponse;

import java.util.List;

public interface PaymentDestinationService {

    List<PaymentDestinationResponse> getPaymentDestinations();

}
