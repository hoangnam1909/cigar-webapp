package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.entity.Order;

import java.util.Map;

public interface PaymentGatewayService {

    Map<String, Object> createPayment(Order order);

    boolean checkTransactionStatus(Long orderId, Map<String, String> params);

    boolean checkTransactionStatus(Order order);

}
