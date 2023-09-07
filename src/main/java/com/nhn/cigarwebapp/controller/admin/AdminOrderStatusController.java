package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.OrderStatusRequest;
import com.nhn.cigarwebapp.dto.response.OrderStatusResponse;
import com.nhn.cigarwebapp.service.OrderStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequestMapping("/api/v1/admin/order-statuses")
public class AdminOrderStatusController {

    @Autowired
    private OrderStatusService orderStatusService;

    @GetMapping
    public ResponseEntity<ResponseObject> getOrderStatuses() {
        List<OrderStatusResponse> orderStatusesResponses = orderStatusService.getOrderStatuses();
        if (orderStatusesResponses.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order statuses found")
                            .result(orderStatusesResponses)
                            .build());
    }

    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ResponseObject> addOrderStatuses(@RequestBody List<OrderStatusRequest> requestList) {
        try {
            orderStatusService.addOrderStatuses(requestList);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Order statuses have been saved")
                            .result(null)
                            .build());
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Could not save your order statuses")
                            .result(ex.getMessage())
                            .build());
        }
    }

}
