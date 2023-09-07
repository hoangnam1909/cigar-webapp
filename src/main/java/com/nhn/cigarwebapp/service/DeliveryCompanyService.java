package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.response.admin.DeliveryCompanyAdminResponse;

import java.util.List;

public interface DeliveryCompanyService {

    List<DeliveryCompanyAdminResponse> getDeliveryCompanies();

}
