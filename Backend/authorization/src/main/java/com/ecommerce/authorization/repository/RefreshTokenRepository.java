package com.ecommerce.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.authorization.dao.RefreshToken;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String>{
}