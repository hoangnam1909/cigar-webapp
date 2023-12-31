package com.nhn.cigarwebapp.specification.product;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.entity.*;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification implements Specification<Product> {

    private List<SearchCriteria> criteriaList;

    public ProductSpecification() {
        this.criteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : criteriaList) {
            if (criteria.getOperation().equals(SearchOperation.MATCH)) {

                predicates.add(builder.like(
                        builder.function("unaccent", String.class, builder.lower(root.get(criteria.getKey()))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%"));

            } else if (criteria.getOperation().equals(SearchOperation.CATEGORY_ID)) {

                Join<Category, Product> categoryProductJoin = root.join(Product_.CATEGORY);
                predicates.add(builder.equal(categoryProductJoin.get(Category_.ID), criteria.getValue().toString()));

            } else if (criteria.getOperation().equals(SearchOperation.BRAND_ID)) {

                Join<Brand, Product> brandProductJoin = root.join(Product_.BRAND);
                predicates.add(builder.equal(brandProductJoin.get(Brand_.ID), criteria.getValue().toString()));

            } else if (criteria.getOperation().equals(SearchOperation.IS_ACTIVE)) {

                predicates.add(builder.equal(
                        root.get(criteria.getKey()).as(Boolean.class), Boolean.parseBoolean((String) criteria.getValue())));

            } else if (criteria.getOperation().equals(SearchOperation.ID_NAME)) {

                Predicate predicateId = builder.like(
                        builder.function("unaccent", String.class, builder.lower(root.get(Product_.ID).as(String.class))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%");

                Predicate predicateName = builder.like(
                        builder.function("unaccent", String.class, builder.lower(root.get(Product_.NAME))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%");

                predicates.add(builder.or(predicateId, predicateName));

            }
            if (criteria.getOperation().equals(SearchOperation.IN_STOCK)) {

                if (Boolean.parseBoolean((String) criteria.getValue())) {
                    predicates.add(builder.greaterThan(
                            root.get(Product_.UNITS_IN_STOCK), 0));
                } else {
                    predicates.add(builder.lessThanOrEqualTo(
                            root.get(Product_.UNITS_IN_STOCK), 0));
                }

            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
