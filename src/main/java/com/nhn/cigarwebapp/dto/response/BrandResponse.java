package com.nhn.cigarwebapp.dto.response;

public record BrandResponse(
        Long id,
        String name,
        String image,
        String country,
        Long productsCount
) {
}
