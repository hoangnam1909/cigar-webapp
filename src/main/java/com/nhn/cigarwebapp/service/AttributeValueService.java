package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.entity.AttributeValue;
import com.nhn.cigarwebapp.model.request.productAttributeValue.AttributeValueRequest;
import com.nhn.cigarwebapp.model.response.attributeValue.AttributeValueResponse;

import java.util.List;

public interface AttributeValueService {

    AttributeValue add(AttributeValueRequest request);

}
