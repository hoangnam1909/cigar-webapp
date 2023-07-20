package com.nhn.cigarwebapp.model.request.brand;

public record BrandRequest(
        String name,
        String description,
        String country
) {
}
