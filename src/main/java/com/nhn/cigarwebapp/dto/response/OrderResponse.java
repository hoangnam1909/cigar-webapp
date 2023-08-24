package com.nhn.cigarwebapp.dto.response;

import com.nhn.cigarwebapp.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long id;
    private CustomerResponse customer;
    private Date createdAt;
    private Double total;
    private String note;
    private Set<OrderItem> orderItems;

}
