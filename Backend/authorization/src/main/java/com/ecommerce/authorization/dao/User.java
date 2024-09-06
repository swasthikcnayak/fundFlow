package com.ecommerce.authorization.dao;

import java.sql.Date;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="USER_ID", unique = true)
    private UUID id;

    @Column(name="EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false)
    private String password;

    @Builder.Default
    @Column(name = "IS_VERIFIED", nullable = false, columnDefinition = "boolean default false")
    private Boolean isVerified = false;

    @Builder.Default
    @Column(name="LAST_LOGGED_IN", nullable = true)
    private Date lastLoggedIn = null;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name="CREATED_AT", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp(source = SourceType.DB)
    @Column(name="UPDATED_AT", nullable = false)
    private Instant updatedAt;

}
