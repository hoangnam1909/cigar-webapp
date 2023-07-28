package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.request.brand.BrandRequest;
import com.nhn.cigarwebapp.model.response.brand.BrandDetailResponse;
import com.nhn.cigarwebapp.model.response.brand.BrandResponse;
import com.nhn.cigarwebapp.model.response.product.ProductResponse;
import com.nhn.cigarwebapp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping
    public ResponseEntity<ResponseObject> getBrands() {

        List<BrandResponse> brands = brandService.getBrands();

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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> brandDetail(@PathVariable(name = "id") String id) {
        BrandDetailResponse brandDetail = brandService.getBrandDetail(Long.valueOf(id));

        if (brandDetail != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Brand found with id = " + id)
                            .result(brandDetail)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());

    }

    @GetMapping("/{id}/products")
    public ResponseEntity<ResponseObject> productsOfBrand(@PathVariable(name = "id") String id) {
        List<ProductResponse> products = brandService.getProductOfBrand(Long.valueOf(id));

        if (!products.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Products of brand with id = " + id)
                            .result(products)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());

    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertBrand(@RequestBody BrandRequest request) {
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

}
