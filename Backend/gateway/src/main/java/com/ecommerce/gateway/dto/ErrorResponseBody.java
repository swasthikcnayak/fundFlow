package com.ecommerce.gateway.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseBody {
    int statusCode;
    String message;
    String extraMessage;

    public ErrorResponseBody(ErrorResponseException e) {
        this.statusCode = e.getStatusCode();
        this.message = e.getMessage();
        this.extraMessage = e.getExtraMessage();
    }
}