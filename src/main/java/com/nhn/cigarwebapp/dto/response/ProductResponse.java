package com.nhn.cigarwebapp.dto.response;

import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.ProductImage;

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
