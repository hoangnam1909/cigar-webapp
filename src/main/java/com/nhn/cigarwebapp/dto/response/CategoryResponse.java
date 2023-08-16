package com.nhn.cigarwebapp.dto.response;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        Long productsCount
) {
}
