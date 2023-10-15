package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.entity.Payment_;
import com.nhn.cigarwebapp.repository.OrderRepository;
import com.nhn.cigarwebapp.repository.PaymentRepository;
import com.nhn.cigarwebapp.service.PaymentGatewayService;
import com.nhn.cigarwebapp.service.PaymentService;
import com.nhn.cigarwebapp.specification.payment.PaymentEnum;
import com.nhn.cigarwebapp.specification.payment.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public boolean updatePaymentStatus(Map<String, String> params) {
        PaymentSpecification specification = new PaymentSpecification();
        Payment payment;

        if (params.containsKey(PaymentEnum.MOMO_REQUEST_ID)) {
            specification.add(new SearchCriteria(Payment_.PAYMENT_ORDER_ID, params.get(PaymentEnum.PAYMENT_ORDER_ID), SearchOperation.EQUAL));
            specification.add(new SearchCriteria(Payment_.REFERENCE_ID, params.get(PaymentEnum.MOMO_REQUEST_ID), SearchOperation.EQUAL));

            List<Payment> payments = paymentRepository.findAll(specification);
            if (payments.size() == 1) {
                payment = payments.get(0);
                boolean isPaid = momoService.checkTransactionStatus(params);
                payment.setIsPaid(isPaid);
                paymentRepository.save(payment);

                return isPaid;
            }
        } else if (params.containsKey(PaymentEnum.VNPAY_REFERENCE_ID)) {
            specification.add(new SearchCriteria(Payment_.REFERENCE_ID, params.get(PaymentEnum.VNPAY_REFERENCE_ID), SearchOperation.EQUAL));

            List<Payment> payments = paymentRepository.findAll(specification);
            if (payments.size() == 1) {
                payment = payments.get(0);
                boolean isPaid = vnPayService.checkTransactionStatus(params);
                payment.setIsPaid(isPaid);
                paymentRepository.save(payment);

                return isPaid;
            }
        } else {
            throw new IllegalArgumentException("Return URL is not valid");
        }

        return false;
    }

}
