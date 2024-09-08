package com.fundflow.authorization.dao;

import java.util.Date;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @Column(name = "REFRESH_TOKEN", unique = true, nullable = false)
    private String token;

    @Column(name = "USER_ID", nullable = false)
    private UUID id;

    @Column(name = "EXPIRY", nullable = false)
    private Date expiry;
}
