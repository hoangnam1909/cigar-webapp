package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.model.AttributeValue;
import com.nhn.cigarwebapp.dto.request.AttributeValueRequest;

public interface AttributeValueService {

    AttributeValue add(AttributeValueRequest request);

}
