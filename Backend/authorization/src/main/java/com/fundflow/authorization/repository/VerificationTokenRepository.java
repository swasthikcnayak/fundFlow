package com.fundflow.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundflow.authorization.dao.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
}
