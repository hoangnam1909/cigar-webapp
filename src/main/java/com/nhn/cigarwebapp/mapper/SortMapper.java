package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.common.SortEnum;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SortMapper {

    public Sort getSort(Map<String, String> params) {
        if (params.containsKey("sort")) {
            if (params.get("sort").equals(SortEnum.PRICE_DESC)) {
                return Sort.by(Sort.Order.desc("salePrice"));
            } else if (params.get("sort").equals(SortEnum.PRICE_ASC)) {
                return Sort.by(Sort.Order.asc("salePrice"));
            } else if (params.get("sort").equals(SortEnum.NEWEST)) {
                return Sort.by(Sort.Order.desc("createdDate"));
            }
        }

        return null;
    }

}
