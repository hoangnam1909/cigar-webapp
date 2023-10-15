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

    @Value("${payment.momo.vnp_QueryUrl}")
    private String queryUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Map createPayment() {
        return null;
    }

    @Override
    public Map createPayment(Order order) {
        long amount = (long) (order.getTotalPrice() * 100 / 1000);
        String referenceId = String.format("19%s%s",
                RandomStringUtils.randomNumeric(8),
                order.getId());

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpVersion);
        vnp_Params.put("vnp_Command", vnpCommandPay);
        vnp_Params.put("vnp_TmnCode", vnpTmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
//        vnp_Params.put("vnp_BankCode", "NCB");
//
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        vnp_Params.put("vnp_CurrCode", vnpCurrCode);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_OrderInfo", "Cigar For Boss Thanh toan don hang #" + order.getId());
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_ReturnUrl", returnUrl + "/payment-result");
        vnp_Params.put("vnp_TxnRef", referenceId);

//        vnp_Params.put("vnp_OrderType", "130008");
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
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
        System.err.println(hashData);
        String queryUrl = query.toString();
        String vnp_SecureHash = HashHelper.HmacSHA512(hashData.toString(), secretKey);

        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = vnpPayUrl + "?" + queryUrl;

        Map map = new HashMap();
        map.put("payUrl", paymentUrl);
        map.put("referenceId", referenceId);

        return map;
    }

    @Override
    public boolean checkTransactionStatus(Map<String, String> params) {
        if (params.containsKey(PaymentEnum.VNPAY_REFERENCE_ID)) {
            String referenceId = params.get(PaymentEnum.VNPAY_REFERENCE_ID);
            String vnp_RequestId = RandomStringUtils.randomAlphanumeric(8);
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

            VNPayQueryRequest request = VNPayQueryRequest.builder()
                    .vnpRequestId(vnp_RequestId)
                    .vnpVersion(vnpVersion)
                    .vnpCommand(vnpCommandQuery)
                    .vnpTmnCode(vnpTmnCode)
                    .vnpTxnRef(referenceId)
                    .vnpOrderInfo("Kiem tra ket qua thanh toan giao dich" + referenceId)
                    .vnpTransactionDate(Long.valueOf(formatter.format(cld.getTime())))
                    .vnpIpAddr("127.0.0.1")
                    .build();

            request.createSignature(secretKey);

            RestTemplate template = new RestTemplate();
            String response = template.postForObject(
                    queryUrl,
                    request,
                    String.class);

            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                Map map = objectMapper.convertValue(jsonNode, Map.class);
                return map.get("vnp_ResponseCode").toString().equals("00");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return false;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "OrderAdminResponse", allEntries = true),
    })
    public boolean checkTransactionStatus(Order order) {
        Payment payment = order.getPayment();

        String vnp_RequestId = RandomStringUtils.randomAlphanumeric(8);
        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        VNPayQueryRequest request = VNPayQueryRequest.builder()
                .vnpRequestId(vnp_RequestId)
                .vnpVersion(vnpVersion)
                .vnpCommand(vnpCommandQuery)
                .vnpTmnCode(vnpTmnCode)
                .vnpTxnRef(payment.getReferenceId())
                .vnpOrderInfo("Kiem tra ket qua thanh toan don hang #" + order.getId())
                .vnpTransactionDate(Long.valueOf(formatter.format(cld.getTime())))
                .vnpIpAddr("127.0.0.1")
                .build();

        request.createSignature(secretKey);

        RestTemplate template = new RestTemplate();
        String response = template.postForObject(
                queryUrl,
                request,
                String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            Map map = objectMapper.convertValue(jsonNode, Map.class);
            return map.get("vnp_ResponseCode").toString().equals("00");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean confirmPayment(Map<String, String> params) {
        return false;
    }

}
