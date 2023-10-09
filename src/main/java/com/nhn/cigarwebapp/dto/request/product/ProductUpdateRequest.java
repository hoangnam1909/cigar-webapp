package com.nhn.cigarwebapp.dto.request.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductUpdateRequest {

    private String name;
    private String description;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer unitsInStock;
    private Long categoryId;
    private Long brandId;

}
