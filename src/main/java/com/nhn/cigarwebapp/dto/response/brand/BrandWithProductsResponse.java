package com.nhn.cigarwebapp.dto.response.brand;

import com.nhn.cigarwebapp.dto.response.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandWithProductsResponse implements Serializable {

    private Long id;
    private String name;
    private String description;
    private String image;
    private String country;
    private List<ProductResponse> products;

}
