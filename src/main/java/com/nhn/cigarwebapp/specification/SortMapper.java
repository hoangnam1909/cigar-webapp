package com.nhn.cigarwebapp.specification;

import com.nhn.cigarwebapp.model.common.SortEnum;
import com.nhn.cigarwebapp.model.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SortMapper {

    public Sort getSort(Map<String, String> params) {
        if (params.containsKey("sort")) {
            if (params.get("sort").equals(SortEnum.PRICE_DESC)) {
                return Sort.sort(Product.class)
                        .by(Product::getSalePrice)
                        .descending();
            } else if (params.get("sort").equals(SortEnum.PRICE_ASC)) {
                return Sort.sort(Product.class)
                        .by(Product::getSalePrice)
                        .ascending();
            } else if (params.get("sort").equals(SortEnum.NEWEST)) {
                return Sort.sort(Product.class)
                        .by(Product::getCreatedDate)
                        .descending();
            }
        }

        return null;
    }

}
