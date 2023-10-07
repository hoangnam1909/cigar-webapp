package com.nhn.cigarwebapp.specification.order;

import com.nhn.cigarwebapp.common.SearchCriteria;
import com.nhn.cigarwebapp.common.SearchOperation;
import com.nhn.cigarwebapp.entity.Order;
import com.nhn.cigarwebapp.entity.*;
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
            } else if (criteria.getOperation().equals(SearchOperation.DELIVERY_COMPANY_ID)) {
                Join<Shipment, Order> shipment = root.join("shipment");
                Join<DeliveryCompany, Shipment> deliveryCompany = shipment.join("deliveryCompany");

                predicates.add(builder.equal(deliveryCompany.get("id"), criteria.getValue().toString()));
            } else if (criteria.getOperation().equals(SearchOperation.ID_NAME)) {
                Predicate predicateId = builder.like(
                        builder.function("unaccent", String.class, builder.lower(root.get("id").as(String.class))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%");

                Join<Customer, Order> customer = root.join("customer");
                Predicate predicateName = builder.like(
                        builder.function("unaccent", String.class, builder.lower(customer.get("fullName"))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%");

                predicates.add(builder.or(predicateId, predicateName));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
