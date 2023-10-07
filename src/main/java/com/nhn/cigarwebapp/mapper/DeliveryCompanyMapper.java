package com.nhn.cigarwebapp.mapper;

import com.nhn.cigarwebapp.dto.response.admin.DeliveryCompanyAdminResponse;
import com.nhn.cigarwebapp.entity.DeliveryCompany;
import org.springframework.stereotype.Service;

@Service

public class DeliveryCompanyMapper {

    public DeliveryCompanyAdminResponse toResponse(DeliveryCompany deliveryCompany){
        return DeliveryCompanyAdminResponse.builder()
                .id(deliveryCompany.getId())
                .name(deliveryCompany.getName())
                .build();
    }

}
