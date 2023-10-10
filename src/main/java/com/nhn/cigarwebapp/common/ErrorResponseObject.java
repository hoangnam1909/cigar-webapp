package com.nhn.cigarwebapp.common;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseObject {

    private String message;
    private String errorCode;
    private Date time;

}
