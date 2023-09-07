package com.nhn.cigarwebapp.dto.response.admin;

import com.nhn.cigarwebapp.dto.response.AttributeValueResponse;
import com.nhn.cigarwebapp.dto.response.BrandResponse;
import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductAdminResponse {

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
    private List<ProductImage> productImages;
    private List<AttributeValueResponse> attributes;

}
