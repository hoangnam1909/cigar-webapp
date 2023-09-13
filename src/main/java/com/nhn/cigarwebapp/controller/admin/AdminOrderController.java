package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/orders")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrders(@RequestParam Map<String, String> params) {
        Page<OrderAdminResponse> orders = orderService.getAdminOrders(params);
        if (!orders.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Orders found")
                            .result(orders)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No orders")
                            .result(null)
                            .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getOrder(@PathVariable String id) {
        OrderAdminResponse orderResponse = orderService.getAdminOrder(Long.valueOf(id));
        if (orderResponse != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order found with id = " + id)
                            .result(orderResponse)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No order with id = " + id)
                            .result(null)
                            .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> partialUpdateOrder(@PathVariable String id,
                                                             @RequestBody Map<String, Object> params) {
        try {
            OrderAdminResponse orderAdminResponse = orderService.partialUpdateOrder(Long.valueOf(id), params);
            if (orderAdminResponse != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Order have been updated")
                                .result(orderAdminResponse)
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Could not update your order")
                                .result(null)
                                .build());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Something went wrong!!!")
                            .result(ex.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order have been deleted")
                            .result(null)
                            .build());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Something went wrong!!!")
                            .result(ex.getMessage())
                            .build());
        }
    }

}
