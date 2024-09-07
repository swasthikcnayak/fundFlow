package com.fundflow.authorization.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JWTResource {
    private String token;
    private Date tokenExpiry;
    private String refreshToken;
    private Date refreshTokenExpiry;
}
