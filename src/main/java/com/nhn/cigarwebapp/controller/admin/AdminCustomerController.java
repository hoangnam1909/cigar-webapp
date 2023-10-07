package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.customer.CustomerRequest;
import com.nhn.cigarwebapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ResponseObject> getCustomers() {
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Customers found")
                        .result(customerService.getCustomers())
                        .build());
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
