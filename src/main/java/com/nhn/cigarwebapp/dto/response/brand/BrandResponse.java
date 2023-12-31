package com.nhn.cigarwebapp.dto.response.brand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse implements Serializable {

    private Long id;
    private String name;
    private String image;
    private String country;

}