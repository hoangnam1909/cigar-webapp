package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.entity.ProductAttribute;
import com.nhn.cigarwebapp.model.request.productAttribute.ProductAttributeRequest;
import com.nhn.cigarwebapp.model.response.productAttribute.ProductAttributeResponse;
import com.nhn.cigarwebapp.service.ProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/product-attributes")
public class ProductAttributeController {

    @Autowired
    private ProductAttributeService productAttributeService;

    @GetMapping
    public ResponseEntity<ResponseObject> getProductAttributes() {

        List<ProductAttributeResponse> responses = productAttributeService.getProductAttributes();

        if (!responses.isEmpty())
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Product attributes founds")
                            .result(responses)
                            .build());
        else
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("No content")
                            .result(List.of())
                            .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject> insertProductAttributes(@RequestBody ProductAttributeRequest request) {
        try {
            productAttributeService.addProductAttribute(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your product attribute have been saved")
                            .result("")
                            .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your product attribute")
                            .result(e.getMessage())
                            .build());
        }
    }

}
