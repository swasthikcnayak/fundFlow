package com.ecommerce.authorization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.authorization.dao.Token;


public interface TokenRepository extends JpaRepository<Token, String>{
}