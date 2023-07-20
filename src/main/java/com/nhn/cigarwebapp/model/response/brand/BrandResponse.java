package com.nhn.cigarwebapp.model.response.brand;

public record BrandResponse(
        Long id,
        String name,
        String image,
        String country
) {
}
