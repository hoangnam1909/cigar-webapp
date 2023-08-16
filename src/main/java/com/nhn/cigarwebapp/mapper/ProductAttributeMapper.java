package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.ProductAttribute;
import com.nhn.cigarwebapp.dto.request.ProductAttributeRequest;
import com.nhn.cigarwebapp.dto.response.ProductAttributeResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductAttributeMapper {

    public ProductAttribute toEntity(ProductAttributeRequest request) {
        return ProductAttribute.builder()
                .name(request.name())
                .build();
    }

    public ProductAttributeResponse toResponse(ProductAttribute productAttribute) {
        return new ProductAttributeResponse(
                productAttribute.getId(),
                productAttribute.getName()
        );
    }

}
