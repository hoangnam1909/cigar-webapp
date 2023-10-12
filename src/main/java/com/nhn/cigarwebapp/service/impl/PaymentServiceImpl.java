package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.repository.OrderRepository;
import com.nhn.cigarwebapp.repository.PaymentRepository;
import com.nhn.cigarwebapp.service.MomoService;
import com.nhn.cigarwebapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.momo.prefix-orderid}")
    public String prefixOrderId;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final MomoService momoService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#orderId"),
            @CacheEvict(value = "OrderAdminResponse", key = "#orderId"),
    })
    public void updatePaymentStatus(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Payment payment = order.getPayment();
            if (payment.getPaymentDestination().getId().equals("cod")) {
                throw new IllegalArgumentException("Can not update payment result with 'COD' payment destination");
            }

            payment.setIsPaid(momoService.checkTransactionStatus(order));
            paymentRepository.save(payment);
        } else {
            throw new IllegalArgumentException("Could not found order with id " + orderId);
        }

    }
}
