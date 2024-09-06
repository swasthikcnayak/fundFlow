package com.ecommerce.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseException extends Throwable {
    private int statusCode;
    private String message;
    private String extraMessage;

    public ErrorResponseException(ErrorResponseException e) {
        this.statusCode = e.statusCode;
        this.message = e.message;
        this.extraMessage = e.extraMessage;
    }
}