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

                Join<OrderStatus, Order> orderStatus = root.join(Order_.ORDER_STATUS);
                predicates.add(builder.equal(orderStatus.get(OrderStatus_.ID), criteria.getValue().toString()));

            } else if (criteria.getOperation().equals(SearchOperation.DELIVERY_COMPANY_ID)) {

                Join<Shipment, Order> shipment = root.join(Order_.SHIPMENT);
                Join<DeliveryCompany, Shipment> deliveryCompany = shipment.join(Shipment_.DELIVERY_COMPANY);

                predicates.add(builder.equal(deliveryCompany.get(DeliveryCompany_.ID), criteria.getValue().toString()));

            } else if (criteria.getOperation().equals(SearchOperation.ID_NAME)) {

                Predicate predicateId = builder.like(
                        builder.function("unaccent", String.class, builder.lower(root.get(Order_.ID).as(String.class))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%");

                Join<Customer, Order> customer = root.join(Order_.CUSTOMER);
                Predicate predicateName = builder.like(
                        builder.function("unaccent", String.class, builder.lower(customer.get(Customer_.FULL_NAME))),
                        "%" + StringUtils.stripAccents(criteria.getValue().toString().toLowerCase().trim()) + "%");

                predicates.add(builder.or(predicateId, predicateName));

            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

}
