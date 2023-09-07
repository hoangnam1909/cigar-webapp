package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.OrderRequest;
import com.nhn.cigarwebapp.dto.response.OrderResponse;
import com.nhn.cigarwebapp.model.Order;
import com.nhn.cigarwebapp.service.OrderService;
import com.nhn.cigarwebapp.specification.SpecificationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/tracking")
    public ResponseEntity<ResponseObject> trackingOrder(@RequestParam Map<String, String> params) {
        if (params.containsKey("orderId") && params.containsKey("phone")) {
            OrderResponse orderResponse = orderService.getOrder(Long.valueOf(params.get("orderId")));

            if (orderResponse != null && orderResponse.getCustomer().getPhone().equals(params.get("phone")))
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Order found with id = " + params.get("orderId"))
                                .result(orderResponse)
                                .build());
            else
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("No order")
                                .result(null)
                                .build());
        }

        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("No order")
                        .result(null)
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> addOrder(@RequestBody OrderRequest request) {
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
