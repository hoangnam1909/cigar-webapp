package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.admin.BrandCreationRequest;
import com.nhn.cigarwebapp.dto.request.admin.BrandUpdateRequest;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.admin.BrandAdminResponse;
import com.nhn.cigarwebapp.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/brands")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminBrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<ResponseObject> getBrands(@RequestParam Map<String, String> params) {
        Page<BrandAdminResponse> brands = brandService.getAdminBrands(params);
        if (!brands.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Brands founds")
                            .result(brands)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(List.of())
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
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your brands")
                            .result(e.getMessage())
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
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your brand")
                            .result(e.getMessage())
                            .build());
        }
    }

}
