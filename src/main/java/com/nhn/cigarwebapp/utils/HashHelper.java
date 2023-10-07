package com.nhn.cigarwebapp.utils;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashHelper {

    public static String HmacSHA256(String data, String secretKey) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey).hmacHex(data);
    }

}
