package com.nhn.cigarwebapp.model.request.category;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CategoryCreationRequest {

    private String name;
    private UUID parentCategoryId;

}
