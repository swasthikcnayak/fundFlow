package com.fundflow.authorization.services;

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

import com.fundflow.authorization.dto.JWTResource;
import com.fundflow.authorization.utils.Constants;
import com.fundflow.authorization.utils.errors.AuthenticationException;
import com.fundflow.authorization.utils.errors.InternalException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecretService {
    private JwtParser jwtParser;

    private SecretKey jwtSignInKey;

    private long jwtExpiration;

    private int passwordStrength;

    private long refreshExpiration;

    public SecretService(Environment environment) {
        this.initJwt(environment);
    }

    public Object extractJwt(String token) throws AuthenticationException {
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

    private Object extractToken(JwtParser parser, String jwtToken) throws AuthenticationException{
        try {
            Jwt<?, ?> jwtObject = parser.parse(jwtToken);
            return jwtObject.getPayload();
        } catch (ExpiredJwtException e) {
            log.error("Expired jwt token", e);
            throw new AuthenticationException("JWT authentication expired");
        } catch (MalformedJwtException | SignatureException | SecurityException | IllegalArgumentException e){
            log.error("Invalid jwt token", e);
            throw new AuthenticationException("Invalid Jwt token");
        }
    }

    public String getPasswordHash(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(this.passwordStrength,
                new SecureRandom());
        return bCryptPasswordEncoder.encode(password);
    }

    public boolean comparePassword(String password, String hash) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(this.passwordStrength,
                new SecureRandom());
        return bCryptPasswordEncoder.matches(password, hash);
    }

    private void initJwt(Environment environment) {
        try{
            this.jwtExpiration = Long.parseLong(Objects.requireNonNull(environment.getProperty(Constants.JWT_EXIPIY)));
            String jwtSecret = Objects.requireNonNull(environment.getProperty(Constants.JWT_SECRET_KEY));
            byte[] secretBytes = Base64.getEncoder().encode(jwtSecret.getBytes());
            this.jwtSignInKey = Keys.hmacShaKeyFor(secretBytes);
            this.jwtParser = Jwts.parser()
                    .verifyWith(this.jwtSignInKey)
                    .build();
            this.refreshExpiration = Long
                    .parseLong(Objects.requireNonNull(environment.getProperty(Constants.REFRESH_EXPIRY)));
            this.passwordStrength = Integer
                    .parseInt(Objects.requireNonNull(environment.getProperty(Constants.PASSWORD_STRENGTH)));
        }catch(WeakKeyException e){
            log.error("Weak secret key");
            throw new InternalException("Key exception");
        }
        catch(NumberFormatException e){
            log.error("Number format exception", e);
            throw new InternalException("Invalid number");
        }catch(NullPointerException e){
            log.error("Null pointer exception", e);
            throw new InternalException("Null pointer exception");
        }
       
    }
}
