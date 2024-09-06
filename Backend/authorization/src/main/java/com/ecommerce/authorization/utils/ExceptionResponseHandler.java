package com.ecommerce.authorization.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.ecommerce.authorization.dto.response.ErrorResponse;
import com.ecommerce.authorization.utils.errors.AuthenticationException;
import com.ecommerce.authorization.utils.errors.BadRequestException;
import com.ecommerce.authorization.utils.errors.InternalException;

import jakarta.validation.ValidationException;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
      BadRequestException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message(ErrorMessages.ERROR_BAD_REQUEST).extraMessage(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
      AuthenticationException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder().statusCode(HttpStatus.UNAUTHORIZED.value()).message(ErrorMessages.ERROR_AUTHENTICATION).extraMessage(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
      ValidationException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder().statusCode(HttpStatus.BAD_REQUEST.value()).message(ErrorMessages.ERROR_VALIDATION).extraMessage(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InternalException.class)
    public ResponseEntity<ErrorResponse> handleInternalException(
      InternalException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.builder().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).message(ErrorMessages.ERROR_VALIDATION).extraMessage(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}


