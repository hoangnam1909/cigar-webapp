package com.nhn.cigarwebapp.service.impl;

import com.nhn.cigarwebapp.mapper.AttributeValueMapper;
import com.nhn.cigarwebapp.model.entity.AttributeValue;
import com.nhn.cigarwebapp.model.request.productAttributeValue.AttributeValueRequest;
import com.nhn.cigarwebapp.repository.AttributeValueRepository;
import com.nhn.cigarwebapp.service.AttributeValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttributeValueServiceImpl implements AttributeValueService {

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private AttributeValueMapper attributeValueMapper;


    @Override
    public AttributeValue add(AttributeValueRequest request) {
        return attributeValueRepository.save(attributeValueMapper.toEntity(request));
    }

}
