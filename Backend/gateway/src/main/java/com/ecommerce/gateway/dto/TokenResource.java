package com.ecommerce.gateway.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResource {
    String id;
    String email;
    Date expiry;
    Date issuedAt;
}
