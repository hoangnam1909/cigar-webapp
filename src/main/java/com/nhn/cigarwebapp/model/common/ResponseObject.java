package com.nhn.cigarwebapp.model.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseObject {

    private String msg;

    private Object data;

}

