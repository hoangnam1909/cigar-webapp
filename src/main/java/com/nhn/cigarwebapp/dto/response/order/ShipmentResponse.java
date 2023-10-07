package com.nhn.cigarwebapp.dto.response.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentResponse {

    private Long id;
    private String address;
    private String trackingNumber;
    private String deliveryCompany;

}
