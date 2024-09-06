package com.ecommerce.authorization.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegistrationResponse {
    String userId;
    String email;
}
