package com.fundflow.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundflow.authorization.dao.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}