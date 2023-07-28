package com.nhn.cigarwebapp.controller;

import com.nhn.cigarwebapp.model.common.ResponseObject;
import com.nhn.cigarwebapp.model.entity.AttributeValue;
import com.nhn.cigarwebapp.model.request.productAttributeValue.AttributeValueRequest;
import com.nhn.cigarwebapp.service.AttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/product-attribute-values")
public class AttributeValueController {

    @Autowired
    private AttributeValueService attributeValueService;


    @PostMapping
    public ResponseEntity<ResponseObject> insertProductAttributes(@RequestBody AttributeValueRequest request) {
        try {
            AttributeValue attributeValue = attributeValueService.add(request);

            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("Your attribute value have been saved")
                            .result(attributeValue)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.ok()
                    .body(ResponseObject.builder()
                            .msg("We could not save your attribute value")
                            .result(e.getMessage())
                            .build());
        }
    }

}
