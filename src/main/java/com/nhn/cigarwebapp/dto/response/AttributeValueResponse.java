package com.nhn.cigarwebapp.dto.response;

public record AttributeValueResponse(
        String value,
        ProductAttributeResponse productAttributeResponse
) {
}
