package com.nhn.cigarwebapp.controller.client;

import com.nhn.cigarwebapp.common.ResponseObject;
import com.nhn.cigarwebapp.dto.request.AttributeValueRequest;
import com.nhn.cigarwebapp.model.AttributeValue;
import com.nhn.cigarwebapp.service.AttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = {"${settings.cors_origin}"})
@RestController
@RequestMapping("/api/v1/product-attribute-values")
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
