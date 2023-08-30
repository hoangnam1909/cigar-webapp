package com.nhn.cigarwebapp.dto.response;

import java.util.List;

public record BrandResponse(
        Long id,
        String name,
        String image,
        String country
) {
}
