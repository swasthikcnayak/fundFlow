package com.ecommerce.authorization.dao;

import java.util.Date;
import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tokens")
public class Token {

    @Id
    @Length(max = 300) 
    @Column(name="TOKEN", unique = true, nullable = false)
    private String token;


    @Column(name="USER_ID", nullable = false)
    private UUID id;

    @Column(name="EXPIRY", nullable = false)
    private Date expiry;
}
