package com.nhn.cigarwebapp.model.response.product;

import com.nhn.cigarwebapp.model.entity.Category;
import com.nhn.cigarwebapp.model.entity.ProductImage;
import com.nhn.cigarwebapp.model.response.brand.BrandResponse;
import com.nhn.cigarwebapp.model.response.attributeValue.AttributeValueResponse;

import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Integer originalPrice,
        Integer salePrice,
        Integer unitsInStock,
        Category category,
        BrandResponse brand,
        List<ProductImage> productImages,
        List<AttributeValueResponse> attributes
) {
}
