package com.nhn.cigarwebapp.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseObject {

    private String msg;

    private Object result;

}

