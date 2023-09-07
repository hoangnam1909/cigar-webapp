package com.nhn.cigarwebapp.specification.order;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.model.*;
import com.nhn.cigarwebapp.model.Order;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification implements Specification<Order> {

    private List<SearchCriteria> criteriaList;

    public OrderSpecification() {
        this.criteriaList = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        criteriaList.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        for (SearchCriteria criteria : criteriaList) {
            if (criteria.getOperation().equals(SearchOperation.ORDER_STATUS_ID)) {
                Join<OrderStatus, Order> statusOrderJoin = root.join("orderStatus");
                predicates.add(builder.equal(statusOrderJoin.get("id"), criteria.getValue().toString()));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
