package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.entity.ProductAttribute;
import com.nhn.cigarwebapp.model.request.productAttribute.ProductAttributeRequest;
import com.nhn.cigarwebapp.model.response.productAttribute.ProductAttributeResponse;
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
