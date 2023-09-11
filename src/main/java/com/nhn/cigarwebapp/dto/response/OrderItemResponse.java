package com.nhn.cigarwebapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse implements Serializable {

    private Long id;
    private ProductResponse product;
    private Integer quantity;
    private Date createdAt;

}
