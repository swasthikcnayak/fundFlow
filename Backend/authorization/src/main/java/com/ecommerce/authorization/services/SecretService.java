package com.ecommerce.authorization.services;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.crypto.SecretKey;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.dto.JWTResource;
import com.ecommerce.authorization.utils.Constants;
import com.ecommerce.authorization.utils.errors.BadRequestException;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class SecretService {

    private Environment environment;

    private JwtParser jwtParser;

    private SecretKey jwtSignInKey;

    private long jwtExpiration;

    private int passwordStrength;

    private long refreshExpiration;

    public SecretService(Environment environment) {
        this.environment = environment;
        this.initJwt();
        this.initPasswordConfig();
    }

    public Object extractJwt(String token) {
        return this.extractToken(this.jwtParser, token);
    }

    public JWTResource generateToken(
            String userId,
            Map<String, Object> claims) {
        Date current = new Date();
        Date expiry = new Date(current.getTime() + this.jwtExpiration);
        String refreshToken = UUID.randomUUID().toString();
        Date refreshTokenExpiry = new Date(current.getTime() + this.refreshExpiration);
        String jwtToken = Jwts
                .builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(current)
                .expiration(expiry)
                .signWith(this.jwtSignInKey)
                .compact();
        return new JWTResource(jwtToken, expiry, refreshToken, refreshTokenExpiry);
    }

    private Object extractToken(JwtParser parser, String jwtToken) {
        try {
            Jwt<?, ?> jwtObject = parser.parse(jwtToken);
            Object payload = jwtObject.getPayload();
            return payload;
        } catch (Exception e) {
            throw new BadRequestException("JWT authentication failure");
        }
    }

    public String getPasswordHash(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(this.passwordStrength,
                new SecureRandom());
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        return encodedPassword;
    }

    public boolean comparePassword(String password, String hash) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(this.passwordStrength,
                new SecureRandom());
        return bCryptPasswordEncoder.matches(password, hash);
    }

    private void initJwt() {
        this.jwtExpiration = Long.parseLong(Objects.requireNonNull(environment.getProperty(Constants.JWT_EXIPIY)));
        String jwtSecret = Objects.requireNonNull(environment.getProperty(Constants.JWT_SECRET_KEY));
        byte[] secretBytes = Base64.getEncoder().encode(jwtSecret.getBytes());
        this.jwtSignInKey = Keys.hmacShaKeyFor(secretBytes);
        this.jwtParser = Jwts.parser()
                .verifyWith(this.jwtSignInKey)
                .build();
        this.refreshExpiration = Long
                .parseLong(Objects.requireNonNull(environment.getProperty(Constants.REFRESH_EXPIRY)));
    }

    private void initPasswordConfig() {
        this.passwordStrength = Integer
                .parseInt(Objects.requireNonNull(environment.getProperty(Constants.PASSWORD_STRENGTH)));
    }

}
