package com.ecommerce.authorization.dto.response;


import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TokenResource {
    String id;
    String email;
    Date expiry;
    Date issuedAt;
}
