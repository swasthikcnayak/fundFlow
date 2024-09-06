package com.ecommerce.authorization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
    private String extraMessage;
}