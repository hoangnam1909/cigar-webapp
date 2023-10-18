package com.nhn.cigarwebapp.service;

import java.util.Map;

public interface PaymentService {

    boolean updatePaymentStatus(Long orderId);

    boolean updatePaymentStatus(Long orderId, Map<String, String> params);

    void recreatePaymentUrl(Long orderId);


}
