package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrders() {

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Orders found")
                        .result(orderService.getOrders())
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertProducts(@RequestBody OrderRequest request) {
        try {
            Order order = orderService.addOrder(request);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your order have been saved")
                            .result(order)
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not save your order")
                            .result(ex.getMessage())
                            .build());
        }

    }

}
