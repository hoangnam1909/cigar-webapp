package com.nhn.cigarwebapp.service;

import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface VNPayService {

    public ResponseEntity<?> createPayment() throws UnsupportedEncodingException;

}
