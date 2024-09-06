package com.ecommerce.authorization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResource{
    String userId;
    String token;
}
