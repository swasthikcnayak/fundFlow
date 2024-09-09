package com.fundflow.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseException extends Exception {
    private final int statusCode;
    private final String message;
    private final String extraMessage;

    public ErrorResponseException(ErrorResponseException e) {
        this.statusCode = e.statusCode;
        this.message = e.message;
        this.extraMessage = e.extraMessage;
    }
}