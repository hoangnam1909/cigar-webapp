package com.nhn.cigarwebapp.model.response.brand;

public record BrandDetailResponse(
        Long id,
        String name,
        String description,
        String image,
        String country
) {

}
