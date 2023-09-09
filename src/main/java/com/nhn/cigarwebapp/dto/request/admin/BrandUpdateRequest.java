package com.nhn.cigarwebapp.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrandUpdateRequest {

    private String name;
    private String link;
    private String description;
    private String image;
    private String country;
    private Integer isBestSeller;

}
