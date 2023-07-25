package com.nhn.cigarwebapp.specification;

import com.nhn.cigarwebapp.model.common.ProductEnum;
import com.nhn.cigarwebapp.model.common.SearchCriteria;
import com.nhn.cigarwebapp.model.common.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpecificationConverter {

    public ProductSpecification productSpecification(Map<String, String> params) {
        ProductSpecification specification = new ProductSpecification();

        if (params.containsKey(ProductEnum.CATEGORY_ID)) {
            specification.add(new SearchCriteria(ProductEnum.CATEGORY_ID, params.get(ProductEnum.CATEGORY_ID), SearchOperation.CATEGORY_ID));
        }

        if (params.containsKey(ProductEnum.BRAND_ID)) {
            specification.add(new SearchCriteria(ProductEnum.BRAND_ID, params.get(ProductEnum.BRAND_ID), SearchOperation.BRAND_ID));
        }

        return specification;
    }

}
