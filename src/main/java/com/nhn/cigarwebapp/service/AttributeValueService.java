package com.nhn.cigarwebapp.service;

import com.nhn.cigarwebapp.dto.request.AttributeValueRequest;
import com.nhn.cigarwebapp.model.AttributeValue;

public interface AttributeValueService {

    AttributeValue add(AttributeValueRequest request);

}
