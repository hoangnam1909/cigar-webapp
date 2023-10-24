package com.nhn.cigarwebapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhn.cigarwebapp.dto.request.payment.VNPayQueryRequest;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.Payment;
import com.nhn.cigarwebapp.service.PaymentGatewayService;
import com.nhn.cigarwebapp.specification.payment.PaymentEnum;
import com.nhn.cigarwebapp.utils.HashHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("vnPayService")
public class VNPayService implements PaymentGatewayService {

    @Value("${payment.vnpay.vnp_Version}")
    private String vnpVersion;

    @Value("${payment.vnpay.vnp_CommandPay}")
    private String vnpCommandPay;

    @Value("${payment.vnpay.vnp_CommandQuery}")
    private String vnpCommandQuery;

    @Value("${payment.vnpay.vnp_CurrCode}")
    private String vnpCurrCode;

    @Value("${settings.cors_origin}")
    private String returnUrl;

    @Value("${payment.vnpay.vnp_TmnCode}")
    private String vnpTmnCode;

    @Value("${payment.vnpay.secretKey}")
    private String secretKey;

    @Value("${payment.vnpay.vnp_PayUrl}")
    private String vnpPayUrl;

    @Value("${payment.vnpay.vnp_QueryUrl}")
    private String queryUrl;

    @Value("${payment.vnpay.prefix-ref-id}")
    private String vnpPrefixRefId;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private static final String DATE_FORMAT_PATTERN = "yyyyMMddHHmmss";
    private static final String TIMEZONE = "Etc/GMT+7";
    private static final String LOCALHOST_IP_ADDRESS = "127.0.0.1";

    @Override
    public Map<String, Object> createPayment(Order order) {
        long amount = order.getTotalPrice().longValue();

        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommandPay);
        vnpParams.put("vnp_TmnCode", vnpTmnCode);
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        vnpParams.put("vnp_CurrCode", vnpCurrCode);
        vnpParams.put("vnp_IpAddr", LOCALHOST_IP_ADDRESS);
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_OrderInfo", "Cigar For Boss Thanh toan don hang #" + order.getId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_ReturnUrl", returnUrl + "/payment-result");

        String referenceId = String.format("%s%s-%s",
                vnpPrefixRefId,
                order.getId(),
                RandomStringUtils.randomNumeric(5));
        vnpParams.put("vnp_TxnRef", referenceId);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnpParams.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));

                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String vnpQueryUrl = query.toString();
        String vnpSecureHash = HashHelper.HmacSHA512(hashData.toString(), secretKey);

        vnpQueryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnpPayUrl + "?" + vnpQueryUrl;

        Map<String, Object> map = new HashMap<>();
        map.put("payUrl", paymentUrl);
        map.put("referenceId", referenceId);

        return map;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#orderId"),
            @CacheEvict(value = "OrderAdminResponse", key = "#orderId"),
    })
    public boolean checkTransactionStatus(Long orderId, Map<String, String> params) {
        if (params.containsKey(PaymentEnum.VNPAY_REFERENCE_ID)) {
            String referenceId = params.get(PaymentEnum.VNPAY_REFERENCE_ID);

            String vnpRequestId = RandomStringUtils.randomAlphanumeric(10);
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);

            VNPayQueryRequest request = VNPayQueryRequest.builder()
                    .vnp_RequestId(vnpRequestId)
                    .vnp_Version(vnpVersion)
                    .vnp_Command(vnpCommandQuery)
                    .vnp_TmnCode(vnpTmnCode)
                    .vnp_TxnRef(referenceId)
                    .vnp_OrderInfo("Kiem tra ket qua thanh toan giao dich #" + referenceId)
                    .vnp_TransactionDate(Long.valueOf(formatter.format(cld.getTime())))
                    .vnp_CreateDate(Long.valueOf(formatter.format(cld.getTime())))
                    .vnp_IpAddr(LOCALHOST_IP_ADDRESS)
                    .build();

            request.createSignature(secretKey);

            String response = restTemplate.postForObject(
                    queryUrl,
                    request,
                    String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                Map map = objectMapper.convertValue(jsonNode, Map.class);
                if (map.get("vnp_ResponseCode").toString().equals("00")) {
                    return map.getOrDefault("vnp_TransactionStatus", "").toString().equals("00");
                } else {
                    throw new IllegalArgumentException(map.get("vnp_Message").toString());
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return false;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderResponse", key = "#order.getId()"),
            @CacheEvict(value = "OrderAdminResponse", key = "#order.getId()"),
    })
    public boolean checkTransactionStatus(Order order) {
        Payment payment = order.getPayment();

        String vnpRequestId = RandomStringUtils.randomAlphanumeric(12);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE));
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        VNPayQueryRequest request = VNPayQueryRequest.builder()
                .vnp_RequestId(vnpRequestId)
                .vnp_Version(vnpVersion)
                .vnp_Command(vnpCommandQuery)
                .vnp_TmnCode(vnpTmnCode)
                .vnp_TxnRef(payment.getReferenceId())
                .vnp_OrderInfo("Kiem tra ket qua thanh toan don hang #" + order.getId())
                .vnp_TransactionDate(Long.valueOf(formatter.format(cld.getTime())))
                .vnp_CreateDate(Long.valueOf(formatter.format(cld.getTime())))
                .vnp_IpAddr(LOCALHOST_IP_ADDRESS)
                .build();

        request.createSignature(secretKey);

        String response = restTemplate.postForObject(
                queryUrl,
                request,
                String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            Map map = objectMapper.convertValue(jsonNode, Map.class);
            if (map.get("vnp_ResponseCode").toString().equals("00")) {
                return map.getOrDefault("vnp_TransactionStatus", "").toString().equals("00");
            } else {
                throw new IllegalArgumentException(map.get("vnp_Message").toString());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
