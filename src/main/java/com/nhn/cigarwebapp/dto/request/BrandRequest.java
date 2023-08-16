package com.nhn.cigarwebapp.dto.request;

public record BrandRequest(
        String name,
        String description,
        String country
) {
}
