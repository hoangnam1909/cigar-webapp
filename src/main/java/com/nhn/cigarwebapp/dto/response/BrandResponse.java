package com.nhn.cigarwebapp.dto.response;

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

    Long id;
    String name;
    String image;
    String country;

}