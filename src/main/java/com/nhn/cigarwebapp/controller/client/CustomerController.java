package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/validate/{value}")
    public ResponseEntity<ResponseObject> validateCustomerEmail(@PathVariable String value) {
        if (value.contains("@")) {
            boolean emailCheck = customerService.isEmailExisted(value);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Invalid email address")
                            .result(!emailCheck)
                            .build());
        } else {
            boolean phoneCheck = customerService.isPhoneNumberExisted(value);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Invalid phone number")
                            .result(!phoneCheck)
                            .build());
        }
    }

}
