package com.nhn.cigarwebapp.dto.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
public class PaymentResponse implements Serializable {

    private Date createdDate;
    private Long paidAmount;
    private Boolean status;
    private String paymentUrl;
    private PaymentDestinationResponse paymentDestination;

}
