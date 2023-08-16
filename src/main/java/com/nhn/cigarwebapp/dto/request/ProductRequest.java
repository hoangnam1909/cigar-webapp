package com.nhn.cigarwebapp.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequest {

    private String name;
    private String description;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer unitsInStock;
    private Long categoryId;
    private Long brandId;
    private List<String> productImagesLink;

}
