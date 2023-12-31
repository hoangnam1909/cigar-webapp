package com.nhn.cigarwebapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.cigarwebapp.dto.request.payment.MomoOneTimePaymentRequest;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.service.PaymentGatewayService;
import com.nhn.cigarwebapp.utils.HashHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service("momoService")
public class MomoService implements PaymentGatewayService {

    @Value("${payment.momo.partner-code}")
    private String partnerCode;

    @Value("${payment.momo.access-key}")
    private String accessKey;

    @Value("${payment.momo.secret-key}")
    private String secretKey;

    @Value("${settings.cors_origin}")
    private String returnUrl;

    @Value("${payment.momo.ipn-url}")
    private String ipnUrl;

    @Value("${payment.momo.request-type}")
    private String requestType;

    @Value("${payment.momo.payment-url}")
    private String paymentUrl;

    @Value("${payment.momo.query-url}")
    private String queryUrl;

    @Value("${payment.momo.confirm-url}")
    private String confirmUrl;

    @Value("${payment.momo.prefix-orderid}")
    private String momoPrefixOrderId;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Object> createPayment(Order order) {
        long amount = (long) (order.getTotalPrice() / 1000);

        MomoOneTimePaymentRequest request = MomoOneTimePaymentRequest.builder()
                .partnerCode(partnerCode)
                .requestId(String.format("%s.id%s",
                        RandomStringUtils.randomNumeric(10),
                        order.getId()))
                .amount(amount)
                .orderId(String.format("%s%s-%s",
                        momoPrefixOrderId,
                        order.getId(),
                        RandomStringUtils.randomAlphanumeric(5)))
                .orderInfo("Thanh toan don hang #" + order.getId())
                .redirectUrl(returnUrl + "/payment-result")
                .ipnUrl("https://momo.vn")
                .requestType(requestType)
                .extraData("")
                .lang("vi")
                .build();

        request.createSignature(accessKey, secretKey);

        String response = restTemplate.postForObject(
                paymentUrl,
                request,
                String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            return objectMapper.convertValue(jsonNode, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#orderId"),
    })
    public boolean checkTransactionStatus(Long orderId, Map<String, String> params) {
        if (params.containsKey("requestId") && params.containsKey("orderId")) {
            String requestId = params.get("requestId");
            String momoOrderId = params.get("orderId");

            String rawHash = "accessKey=" + accessKey +
                    "&orderId=" + momoOrderId +
                    "&partnerCode=" + partnerCode +
                    "&requestId=" + requestId;
            String signature = HashHelper.HmacSHA256(rawHash, secretKey);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("partnerCode", partnerCode);
            requestBody.put("requestId", requestId);
            requestBody.put("orderId", momoOrderId);
            requestBody.put("signature", signature);
            requestBody.put("lang", "vi");

            String response = restTemplate.postForObject(
                    queryUrl,
                    requestBody,
                    String.class
            );
            System.err.println(response);

            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                Map map = objectMapper.convertValue(jsonNode, Map.class);

                return map.get("resultCode").toString().equals("0");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderAdminResponse", key = "#order.getId()"),
    })
    public boolean checkTransactionStatus(Order order) {
        Payment payment = order.getPayment();

        String requestId = payment.getReferenceId();
        String orderId = payment.getPaymentOrderId();

        String rawHash = "accessKey=" + accessKey +
                "&orderId=" + orderId +
                "&partnerCode=" + partnerCode +
                "&requestId=" + requestId;
        String signature = HashHelper.HmacSHA256(rawHash, secretKey);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("partnerCode", partnerCode);
        requestBody.put("requestId", requestId);
        requestBody.put("orderId", orderId);
        requestBody.put("signature", signature);
        requestBody.put("lang", "vi");

        String response = restTemplate.postForObject(
                queryUrl,
                requestBody,
                String.class
        );
        System.err.println(response);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            Map map = objectMapper.convertValue(jsonNode, Map.class);

            return map.get("resultCode").toString().equals("0");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
