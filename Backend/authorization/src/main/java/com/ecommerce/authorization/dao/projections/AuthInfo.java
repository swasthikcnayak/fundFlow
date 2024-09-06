package com.ecommerce.authorization.dao.projections;

import java.util.UUID;

public interface AuthInfo {
    UUID getId();
    String getEmail();
    String getPassword();
}
