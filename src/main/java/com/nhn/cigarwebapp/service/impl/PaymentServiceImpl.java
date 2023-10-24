package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.repository.OrderRepository;
import com.nhn.cigarwebapp.repository.PaymentRepository;
import com.nhn.cigarwebapp.service.PaymentGatewayService;
import com.nhn.cigarwebapp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${payment.momo.prefix-orderid}")
    public String prefixOrderId;

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Qualifier("momoService")
    private final PaymentGatewayService momoService;

    @Qualifier("vnPayService")
    private final PaymentGatewayService vnPayService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#orderId"),
            @CacheEvict(value = "OrderAdminResponse", key = "#orderId"),
    })
    public boolean updatePaymentStatus(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Payment payment = order.getPayment();
            boolean isPaid = false;

            if (payment.getPaymentDestination().getId().equals("cod")) {
                throw new IllegalArgumentException("Can not update payment result with 'COD' payment destination");
            } else if (payment.getPaymentDestination().getId().equals("momo")) {
                isPaid = momoService.checkTransactionStatus(order);
                payment.setIsPaid(isPaid);
                paymentRepository.save(payment);
            } else if (payment.getPaymentDestination().getId().equals("vnpay")) {
                isPaid = vnPayService.checkTransactionStatus(order);
                payment.setIsPaid(isPaid);
                paymentRepository.save(payment);
            }

            return isPaid;
        } else {
            throw new IllegalArgumentException("Could not found order with id " + orderId);
        }
    }


    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#orderId"),
            @CacheEvict(value = "OrderAdminResponse", key = "#orderId"),
    })
    public boolean updatePaymentStatus(Long orderId, Map<String, String> params) {
        if (orderId < 0) throw new IllegalArgumentException("Return URL is not valid");

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Payment payment = order.getPayment();
            boolean isPaid = false;

            if (payment.getPaymentDestination().getId().equals("momo")) {
                isPaid = momoService.checkTransactionStatus(orderId, params);
                payment.setIsPaid(isPaid);
            } else if (payment.getPaymentDestination().getId().equals("vnpay")) {
                isPaid = vnPayService.checkTransactionStatus(orderId, params);
                payment.setIsPaid(isPaid);
            }

            paymentRepository.save(payment);
            return isPaid;
        } else {
            throw new IllegalArgumentException("Could not found order with id " + orderId);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#orderId"),
            @CacheEvict(value = "OrderAdminResponse", key = "#orderId"),
    })
    public void recreatePaymentUrl(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Payment payment = order.getPayment();
            if (Boolean.FALSE.equals(payment.getIsPaid())) {
                switch (payment.getPaymentDestination().getId()) {
                    case "cod" ->
                            throw new IllegalArgumentException("Can not recreate payment url with Cash on delivery");
                    case "momo" -> {
                        Map<String, Object> momoResponse = momoService.createPayment(order);
                        payment.setIsPaid(false);
                        payment.setReferenceId((String) momoResponse.get("requestId"));
                        payment.setPaymentOrderId((String) momoResponse.get("orderId"));
                        payment.setPaymentUrl((String) momoResponse.get("payUrl"));
                        paymentRepository.save(payment);
                    }
                    case "vnpay" -> {
                        Map<String, Object> vnPayResponse = vnPayService.createPayment(order);
                        payment.setIsPaid(false);
                        payment.setReferenceId((String) vnPayResponse.get("referenceId"));
                        payment.setPaymentUrl((String) vnPayResponse.get("payUrl"));
                        paymentRepository.save(payment);
                    }
                    default -> throw new IllegalArgumentException("Payment destination is invalid");
                }
            }
        } else {
            throw new IllegalArgumentException("Order id do not exist");
        }
    }

}
