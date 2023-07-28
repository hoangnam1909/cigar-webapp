package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.model.entity.AttributeValue;
import com.nhn.cigarwebapp.model.request.productAttributeValue.AttributeValueRequest;
import com.nhn.cigarwebapp.model.response.attributeValue.AttributeValueResponse;
import com.nhn.cigarwebapp.repository.ProductAttributeRepository;
import com.nhn.cigarwebapp.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueMapper {

    @Autowired
    private ProductAttributeRepository productAttributeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductAttributeMapper productAttributeMapper;

    public AttributeValue toEntity(AttributeValueRequest request) {
        return AttributeValue.builder()
                .value(request.value())
                .productAttribute(productAttributeRepository.getReferenceById(request.productAttributeId()))
                .product(productRepository.getReferenceById(request.productId()))
                .build();
    }

    public AttributeValueResponse toProductResponse(AttributeValue attributeValue) {
        return new AttributeValueResponse(
                attributeValue.getValue(),
                productAttributeMapper.toResponse(attributeValue.getProductAttribute())
        );
    }

}
