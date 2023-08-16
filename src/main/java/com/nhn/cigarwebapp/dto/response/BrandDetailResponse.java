package com.nhn.cigarwebapp.dto.response;

public record BrandDetailResponse(
        Long id,
        String name,
        String description,
        String image,
        String country
) {

}
