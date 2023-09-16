package com.nhn.cigarwebapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartProductResponse {

    private Long id;
    private String name;
    private String image;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer unitsInStock;

}
