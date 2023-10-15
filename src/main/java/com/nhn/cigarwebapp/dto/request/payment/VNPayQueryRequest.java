package com.nhn.cigarwebapp.dto.request.payment;

import com.nhn.cigarwebapp.utils.HashHelper;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VNPayQueryRequest {

    private String vnpRequestId;
    private String vnpVersion;
    private String vnpCommand;
    private String vnpTmnCode;
    private String vnpTxnRef;
    private String vnpOrderInfo;
    private Long vnpTransactionDate;
    private Long vnpCreateDate;
    private String vnpIpAddr;
    private String vnpSecureHash;

    public void createSignature(String secretKey) {
        String rawHash = vnpRequestId + "|" +
                vnpVersion + "|" +
                vnpCommand + "|" +
                vnpTmnCode + "|" +
                vnpTxnRef + "|" +
                vnpTransactionDate + "|" +
                vnpCreateDate + "|" +
                vnpIpAddr + "|" +
                vnpOrderInfo;

        vnpSecureHash = HashHelper.HmacSHA512(rawHash, secretKey);
    }

}
