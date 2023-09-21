package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.specification.sort.OrderSortEnum;
import com.nhn.cigarwebapp.specification.sort.ProductSortEnum;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SortMapper {

    public Sort getProductSort(String sort) {
        if (sort != null) {
            switch (sort) {
                case ProductSortEnum.DEFAULT -> {
                    return Sort.by(Sort.Order.desc("unitsInStock")).and(Sort.by(Sort.Order.desc("createdDate")));
                }
                case ProductSortEnum.ADMIN_DEFAULT -> {
                    return Sort.by(Sort.Order.desc("active")).and(Sort.by(Sort.Order.desc("createdDate")));
                }
                case ProductSortEnum.PRICE_DESC -> {
                    return Sort.by(Sort.Order.desc("salePrice"));
                }
                case ProductSortEnum.PRICE_ASC -> {
                    return Sort.by(Sort.Order.asc("salePrice"));
                }
                case ProductSortEnum.NEWEST -> {
                    return Sort.by(Sort.Order.desc("createdDate"));
                }
            }
        }

        return null;
    }

    public Sort getOrderSort(String sort) {
        if (sort != null) {
            if (sort.equals(OrderSortEnum.ORDER_STATUS_DESC))
                return Sort.by(Sort.Order.desc("orderStatus"));

            if (sort.equals(OrderSortEnum.ORDER_STATUS_ASC))
                return Sort.by(Sort.Order.asc("orderStatus"));

            if (sort.equals(OrderSortEnum.CREATED_AT_DESC))
                return Sort.by(Sort.Order.desc("createdAt"));

            if (sort.equals(OrderSortEnum.CREATED_AT_ASC))
                return Sort.by(Sort.Order.asc("createdAt"));
        }

        return null;
    }
}
