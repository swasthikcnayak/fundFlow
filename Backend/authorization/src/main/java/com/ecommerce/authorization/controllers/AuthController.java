package com.ecommerce.authorization.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.authorization.dto.request.UserLoginRequest;
import com.ecommerce.authorization.dto.request.UserRegistrationRequest;
import com.ecommerce.authorization.dto.response.LoginResource;
import com.ecommerce.authorization.dto.response.RefreshResponse;
import com.ecommerce.authorization.dto.response.TokenResource;
import com.ecommerce.authorization.services.AuthService;
import com.ecommerce.authorization.utils.Constants;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.apache.http.auth.AuthenticationException;



@RestController
@RequestMapping("/auth/v1/")
public class AuthController {

    @Autowired
    AuthService authService;
    
    @PostMapping(name="validateToken", value = "/validate", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<TokenResource> validateToken(@RequestHeader(Constants.AUTHORIZATION_HEADER) String authorization) {
        TokenResource userInfo= authService.validateToken(authorization);
        return new ResponseEntity<TokenResource>(userInfo, HttpStatus.OK);
    }

    @PostMapping(name="refreshToken", value = "/refresh", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<RefreshResponse> refresh(@RequestHeader(Constants.AUTHORIZATION_HEADER) String authorization, @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) {
        RefreshResponse response = authService.refreshToken(authorization, refreshToken);
        return new ResponseEntity<RefreshResponse>(response, HttpStatus.OK);
    }
    

    @PostMapping(name="login", value = "/login", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<LoginResource> login(@Valid @RequestBody UserLoginRequest loginRequest) throws AuthenticationException {
        LoginResource resource = authService.login(loginRequest);
        return new ResponseEntity<LoginResource>(resource, HttpStatus.OK);
    }
    
    @PostMapping(name="register", value="/register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationDto) {
        authService.register(userRegistrationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
