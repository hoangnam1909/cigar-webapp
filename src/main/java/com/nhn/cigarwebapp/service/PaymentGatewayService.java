package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.entity.Order;

import java.util.Map;

public interface PaymentGatewayService {

    Map createPayment();

    Map createPayment(Order order);

    boolean checkTransactionStatus(Map<String, String> params);

    boolean checkTransactionStatus(Order order);

    boolean confirmPayment(Map<String, String> params);

}
