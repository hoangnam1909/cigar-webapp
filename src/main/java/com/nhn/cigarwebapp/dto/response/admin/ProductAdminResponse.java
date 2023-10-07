package com.nhn.cigarwebapp.dto.response.admin;

import com.nhn.cigarwebapp.dto.response.brand.BrandResponse;
import com.nhn.cigarwebapp.dto.response.product.ProductImageResponse;
import com.nhn.cigarwebapp.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAdminResponse implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer unitsInStock;
    private Category category;
    private BrandResponse brand;
    private Date createdDate;
    private Date modifiedDate;
    private Boolean active;
    private List<ProductImageResponse> productImages;

}
