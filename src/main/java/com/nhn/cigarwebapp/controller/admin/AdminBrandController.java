package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import com.nhn.cigarwebapp.dto.response.brand.BrandResponse;
import com.nhn.cigarwebapp.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/brands")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBrandController {

    Logger logger = LoggerFactory.getLogger(AdminBrandController.class);

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<ResponseObject> getBrands(@RequestParam Map<String, String> params) {
        Page<BrandAdminResponse> brands = brandService.getAdminBrands(params);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Brands founds")
                        .result(brands)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getBrand(@PathVariable(name = "id") Long id) {
        BrandAdminResponse brand = brandService.getAdminBrand(id);
        if (brand != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Brands founds")
                            .result(brand)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertBrand(@RequestBody BrandCreationRequest request) {
        try {
            brandService.addBrand(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your brands have been saved")
                            .result("")
                            .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateBrand(@PathVariable(name = "id") String id,
                                                      @RequestBody BrandUpdateRequest request) {
        try {
            BrandResponse response = brandService.update(Long.valueOf(id), request);

            if (response != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Your brand have been saved")
                                .result("")
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Something went wrong")
                                .result(null)
                                .build());
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.internalServerError()
                    .body(ResponseObject.builder()
                            .msg("Error!")
                            .result("Internal Server Error")
                            .build());
        }
    }

}
