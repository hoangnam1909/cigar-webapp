package com.nhn.cigarwebapp.model.request.promotion;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PromotionRequest {

    private String name;
    private String description;
    private double discountRate;
    private Date startDate;
    private Date endDate;

}
