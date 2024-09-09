package com.fundflow.authorization.dao;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Entity
@AllArgsConstructor
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "VERFICATION_TOKEN", unique = true, nullable = false)
    private String token;

    @Column(name = "USER_ID", nullable = false)
    private UUID userId;

    @Column(name = "EXPIRY", nullable = false)
    private Date expiry;
}
