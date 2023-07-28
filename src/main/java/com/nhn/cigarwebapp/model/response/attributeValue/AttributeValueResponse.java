package com.nhn.cigarwebapp.model.response.attributeValue;

import com.nhn.cigarwebapp.model.response.product.ProductResponse;
import com.nhn.cigarwebapp.model.response.productAttribute.ProductAttributeResponse;

public record AttributeValueResponse(
        String value,
        ProductAttributeResponse productAttributeResponse
) {
}
