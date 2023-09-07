package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.CustomerRequest;
import com.nhn.cigarwebapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<ResponseObject> getCustomers() {
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Customers found")
                        .result(customerService.getCustomers())
                        .build());
    }

    @GetMapping("/validate/{value}")
    public ResponseEntity<ResponseObject> validateCustomerEmail(@PathVariable String value) {
        if (value.contains("@")) {
            boolean emailCheck = customerService.isEmailExisted(value);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Validate email address")
                            .result(!emailCheck)
                            .build());
        } else {
            boolean phoneCheck = customerService.isPhoneNumberExisted(value);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Valid phone number")
                            .result(!phoneCheck)
                            .build());
        }
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertCustomers(@RequestBody List<CustomerRequest> request) {
        try {
            customerService.addCustomers(request);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Customers info added successfully")
                            .result("")
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error")
                            .result(ex.getMessage())
                            .build());
        }
    }
}
