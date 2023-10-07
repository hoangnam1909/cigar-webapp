package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.entity.Order;

import java.util.Map;

public interface MomoService {

    Map createOrder();

    Map createOrder(Order order);

    boolean checkTransactionStatus(Map<String, String> params);

    boolean confirmPayment(Map<String, String> params);

}
