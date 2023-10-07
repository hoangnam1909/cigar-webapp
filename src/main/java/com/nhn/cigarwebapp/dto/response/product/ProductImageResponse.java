package com.nhn.cigarwebapp.dto.response.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageResponse implements Serializable {

    private Long id;
    private String linkToImage;

}
