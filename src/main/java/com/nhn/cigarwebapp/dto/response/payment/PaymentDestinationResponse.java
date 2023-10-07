package com.nhn.cigarwebapp.dto.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PaymentDestinationResponse implements Serializable {

    private String id;
    private String logo;
    private String shortName;
    private String name;

}
