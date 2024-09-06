package com.ecommerce.gateway.utils;

public interface Constants {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTH_SERVICE_TOKEN_VERIFICATION_ENDPOINT = "lb://authservice/auth/v1/validate";
    public static final String HEADER_CLIENT_ID = "X-clientId";
}
