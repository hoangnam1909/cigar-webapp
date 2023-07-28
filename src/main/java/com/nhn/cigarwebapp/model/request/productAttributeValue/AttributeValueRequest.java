package com.nhn.cigarwebapp.model.request.productAttributeValue;

public record AttributeValueRequest(
        String value,
        Long productAttributeId,
        Long productId
) {
}
