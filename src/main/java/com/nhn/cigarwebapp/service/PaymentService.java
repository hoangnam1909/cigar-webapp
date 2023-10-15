package com.nhn.cigarwebapp.service;

import java.util.Map;

public interface PaymentService {

    boolean updatePaymentStatus(Long orderId);

    boolean updatePaymentStatus(Map<String, String> params);

}
