package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.ProductRequest;
import com.nhn.cigarwebapp.dto.request.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.ProductResponse;
import com.nhn.cigarwebapp.dto.response.admin.OrderAdminResponse;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProducts(@RequestParam Map<String, String> params) {
        Page<ProductAdminResponse> products = productService.getAdminProducts(params);
        return ResponseEntity.ok()
                .body(ResponseObject.builder()
                        .msg("Products found")
                        .result(products)
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertProducts(@RequestBody ProductRequest request) {
        try {
            ProductResponse productResponse = productService.add(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your products have been saved")
                            .result(productResponse)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not save your products")
                            .result(e.getMessage())
                            .build());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject> updateEntireProducts(
            @PathVariable String id,
            @RequestBody ProductUpdateRequest request) {
        try {
            ProductResponse productResponse = productService.update(Long.valueOf(id), request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your product have been updated")
                            .result(productResponse)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not update your product")
                            .result(e.getMessage())
                            .build());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseObject> partialUpdateProduct(@PathVariable Long id,
                                                               @RequestBody Map<String, Object> params) {
        try {
            ProductAdminResponse productAdminResponse = productService.partialUpdateProduct(id, params);
            if (productAdminResponse != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Product have been updated")
                                .result(productAdminResponse)
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("Could not update your product")
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
    public ResponseEntity<ResponseObject> deleteProduct(@PathVariable(name = "id") String id) {
        try {
            productService.delete(Long.valueOf(id));

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg(String.format("Product with id = %s have been deleted", id))
                            .result("")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not delete your product")
                            .result(e.getMessage())
                            .build());
        }
    }

}
