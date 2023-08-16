package com.nhn.cigarwebapp.dto.request;

public record AttributeValueRequest(
        String value,
        Long productAttributeId,
        Long productId
) {
}
