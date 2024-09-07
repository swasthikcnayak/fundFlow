package com.ecommerce.authorization.dto.response;


public class LoginResource extends RefreshResponse{
    String refreshToken;

    public LoginResource(String userId, String token, String refreshToken){
        super(userId, token);
        this.refreshToken = refreshToken;
    }
}
