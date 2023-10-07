package com.nhn.cigarwebapp.controller.admin;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.product.ProductRequest;
import com.nhn.cigarwebapp.dto.request.product.ProductUpdateRequest;
import com.nhn.cigarwebapp.dto.response.admin.ProductAdminResponse;
import com.nhn.cigarwebapp.dto.response.product.ProductResponse;
import com.nhn.cigarwebapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getProduct(@PathVariable Long id) {
        ProductAdminResponse product = productService.getAdminProduct(id);
        if (product != null)
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Products found")
                            .result(product)
                            .build());
        else
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("Product not found")
                            .result(null)
                            .build());
    }

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ResponseObject> addProducts(@RequestPart("product") ProductRequest request,
                                                      @RequestPart("files") List<MultipartFile> files) {
        try {
            ProductResponse productResponse = productService.add(request, files);

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

    @PutMapping(value = "/{id}",
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.MULTIPART_FORM_DATA_VALUE
            })
    public ResponseEntity<ResponseObject> updateEntireProducts(
            @PathVariable String id,
            @RequestPart("product") ProductUpdateRequest request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        try {
            ProductResponse productResponse = productService.update(Long.valueOf(id), request, files);

            if (productResponse != null)
                return ResponseEntity.ok()
                        .body(ResponseObject.builder()
                                .msg("Your product have been updated")
                                .result(productResponse)
                                .build());
            else
                return ResponseEntity.badRequest()
                        .body(ResponseObject.builder()
                                .msg("We could not update your product")
                                .result(null)
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
            productService.partialUpdateProduct(id, params);
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Product have been updated")
                            .result("")
                            .build());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
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
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(ResponseObject.builder()
                            .msg("We could not delete your product")
                            .result("")
                            .build());
        }
    }

}
