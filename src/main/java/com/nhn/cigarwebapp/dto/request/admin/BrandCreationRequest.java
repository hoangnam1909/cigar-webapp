package com.nhn.cigarwebapp.dto.request.admin;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandCreationRequest {

    private String name;
    private String link;
    private String description;
    private String image;
    private String country;

}
