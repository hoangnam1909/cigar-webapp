package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.dto.response.BrandWithProductsResponse;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequestMapping("/api/v1/brands")
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
        BrandResponse brandDetail = brandService.getBrand(Long.valueOf(id));

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
    public ResponseEntity<ResponseObject> productsOfBrand(@PathVariable(name = "id") String id,
                                                          @RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int size = params.containsKey("size") ? Integer.parseInt(params.get("size")) : 4;

        Page<ProductResponse> products = brandService.getProductOfBrand(Long.valueOf(id), page, size);

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

    @GetMapping("/top/{topNumber}")
    @Cacheable(key = "#topNumber", value = "topBrands")
    public ResponseEntity<ResponseObject> getTopBrand(@PathVariable Integer topNumber) {
        List<BrandWithProductsResponse> brandResponses = brandService.getTop(topNumber);
        if (!brandResponses.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Top 3 brands")
                            .result(brandResponses)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(null)
                            .build());
    }

}
