package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.admin.DeliveryCompanyAdminResponse;
import com.nhn.cigarwebapp.service.DeliveryCompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/delivery-companies")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDeliveryCompanyController {

    private final DeliveryCompanyService deliveryCompanyService;

    @GetMapping
    public ResponseEntity<ResponseObject> getDeliveryCompanies() {
        List<DeliveryCompanyAdminResponse> responses = deliveryCompanyService.getDeliveryCompanies();
        if (!responses.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Delivery companies found")
                            .result(responses)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());
    }

}
