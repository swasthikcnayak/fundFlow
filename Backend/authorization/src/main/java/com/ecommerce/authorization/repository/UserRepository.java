package com.ecommerce.authorization.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.authorization.dao.User;
import com.ecommerce.authorization.dao.projections.AuthInfo;


public interface UserRepository extends JpaRepository<User, UUID>{
    AuthInfo findOneByEmailAndIsVerified(String email, boolean isVerified);
}