package com.fundflow.authorization.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginResource extends RefreshResponse{
    String refreshToken;

    public LoginResource(String userId, String token, String refreshToken){
        super(userId, token);
        this.refreshToken = refreshToken;
    }
}
