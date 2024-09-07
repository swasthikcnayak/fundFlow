package com.fundflow.authorization.dto.response;

import lombok.Data;

@Data
public class LoginResource extends RefreshResponse{
    String refreshToken;

    public LoginResource(String userId, String token, String refreshToken){
        super(userId, token);
        this.refreshToken = refreshToken;
    }
}
