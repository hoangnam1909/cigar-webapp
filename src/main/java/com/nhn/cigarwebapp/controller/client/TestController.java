package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class TestController {

    private final EmailService emailService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts() {
//        emailService.sendSimpleMail("send test email", "email body", "dev.nhn1909@gmail.com");
        emailService.sendHtmlEmail("namenenameneee", "dev.nhn1909@gmail.com");
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("email sent")
                        .result(true)
                        .build());
    }

}
