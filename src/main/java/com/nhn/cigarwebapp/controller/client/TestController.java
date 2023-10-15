package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.service.EmailService;
import com.nhn.cigarwebapp.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final EmailService emailService;
    private final FileService fileService;
    //    private final VNPayService vnPayService;
//    private final MomoService momoService;
//    private final PaymentGatewayService paymentGatewayService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts() {
//        emailService.sendSimpleMail("send test email", "email body", "dev.nhn1909@gmail.com");
//        emailService.sendHtmlEmail("namenenameneee", "dev.nhn1909@gmail.com");
        System.err.println(new Date());
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("email sent")
                        .result(true)
                        .build());
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseObject> upload(@RequestPart("file") MultipartFile file) throws IOException {
        String link = fileService.uploadFile(file);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("email sent")
                        .result(link)
                        .build());
    }

    @PostMapping("/uploads")
    public ResponseEntity<ResponseObject> uploads(@RequestPart("file") List<MultipartFile> files) {
        List<String> link = fileService.uploadFiles(files);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("email sent")
                        .result(link)
                        .build());
    }

//    @GetMapping("/payment/vnpay")
//    public ResponseEntity<?> vnPay() {
//        return ResponseEntity.ok(paymentGatewayService.createPayment());
//    }
//
//    @GetMapping("/payment/momo")
//    public ResponseEntity<?> momo() {
//        System.err.println("momo ne");
//        return ResponseEntity.ok(paymentGatewayService.createPayment());
//    }

}
