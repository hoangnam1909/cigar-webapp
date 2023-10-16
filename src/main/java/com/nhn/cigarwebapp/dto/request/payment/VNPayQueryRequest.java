package com.nhn.cigarwebapp.dto.request.payment;

import com.nhn.cigarwebapp.utils.HashHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
public class VNPayQueryRequest {

    private String vnp_RequestId;
    private String vnp_Version;
    private String vnp_Command;
    private String vnp_TmnCode;
    private String vnp_TxnRef;
    private String vnp_OrderInfo;
    private Long vnp_TransactionDate;
    private Long vnp_CreateDate;
    private String vnp_IpAddr;
    private String vnp_SecureHash;

    public void createSignature(String secretKey) {
        String rawHash = vnp_RequestId + "|" +
                vnp_Version + "|" +
                vnp_Command + "|" +
                vnp_TmnCode + "|" +
                vnp_TxnRef + "|" +
                vnp_TransactionDate + "|" +
                vnp_CreateDate + "|" +
                vnp_IpAddr + "|" +
                vnp_OrderInfo;

        vnp_SecureHash = HashHelper.HmacSHA512(rawHash, secretKey);
    }

}
