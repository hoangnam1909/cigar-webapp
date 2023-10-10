package com.nhn.cigarwebapp.common;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseObject {

    private String msg;
    private Object result;

}

