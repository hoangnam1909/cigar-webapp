package com.nhn.cigarwebapp.dto.response;

import com.nhn.cigarwebapp.model.Category;
import com.nhn.cigarwebapp.model.ProductImage;
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
public class ProductResponse implements Serializable {

    private static final long serialVersionUID = 5778414782048504952L;

    private Long id;
    private String name;
    private String description;
    private Integer originalPrice;
    private Integer salePrice;
    private Integer unitsInStock;
    private Category category;
    private BrandResponse brand;
    private Date createdDate;
    private List<ProductImage> productImages;
    private List<AttributeValueResponse> attributes;

}
