package com.fundflow.authorization.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundflow.authorization.dto.request.UserLoginRequest;
import com.fundflow.authorization.dto.request.UserRegistrationRequest;
import com.fundflow.authorization.dto.response.LoginResource;
import com.fundflow.authorization.dto.response.RefreshResponse;
import com.fundflow.authorization.dto.response.TokenResource;
import com.fundflow.authorization.services.AuthService;
import com.fundflow.authorization.services.VerificationService;
import com.fundflow.authorization.utils.Constants;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth/v1/")
public class AuthController {

        AuthService authService;

        VerificationService verificationService;

        public AuthController(AuthService authService, VerificationService verificationService) {
                this.authService = authService;
                this.verificationService = verificationService;
        }

        @PostMapping(name = "validateToken", value = "/validate", produces = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<TokenResource> validateToken(
                        @RequestHeader(Constants.AUTHORIZATION_HEADER) String authorization) {
                TokenResource userInfo = authService.validateToken(authorization);
                return new ResponseEntity<>(userInfo, HttpStatus.OK);
        }

        @PostMapping(name = "refreshToken", value = "/refresh", produces = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<RefreshResponse> refresh(
                        @RequestHeader(Constants.AUTHORIZATION_HEADER) String authorization,
                        @RequestHeader(Constants.REFRESH_HEADER) String refreshToken) {
                RefreshResponse response = authService.refreshToken(authorization, refreshToken);
                return new ResponseEntity<>(response, HttpStatus.OK);
        }

        @PostMapping(name = "login", value = "/login", produces = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<LoginResource> login(@Valid @RequestBody UserLoginRequest loginRequest) {
                LoginResource resource = authService.login(loginRequest);
                return new ResponseEntity<>(resource, HttpStatus.OK);
        }

        @PostMapping(name = "register", value = "/register", produces = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<Void> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationDto) {
                authService.register(userRegistrationDto);
                return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @GetMapping(name = "verify user", value = "/verify/{token}", produces = { MediaType.APPLICATION_JSON_VALUE,
                        MediaType.APPLICATION_XML_VALUE })
        public ResponseEntity<Void> postMethodName(@PathVariable String token) {
                verificationService.verifyUser(token);
                return new ResponseEntity<>(HttpStatus.OK);
        }
}
