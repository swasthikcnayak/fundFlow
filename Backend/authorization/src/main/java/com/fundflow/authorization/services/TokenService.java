package com.fundflow.authorization.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundflow.authorization.dao.RefreshToken;
import com.fundflow.authorization.dao.projections.AuthInfo;
import com.fundflow.authorization.dto.JWTResource;
import com.fundflow.authorization.repository.RefreshTokenRepository;
import com.fundflow.authorization.utils.errors.AuthenticationException;

import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(JWTResource jwtResource, AuthInfo authInfo) {
        CompletableFuture<Void> tokenSaveFuture = CompletableFuture.runAsync(() -> {
            RefreshToken token = new RefreshToken(jwtResource.getRefreshToken().toString(), authInfo.getId(),
                    jwtResource.getRefreshTokenExpiry());
            refreshTokenRepository.save(token);
        });
        tokenSaveFuture = tokenSaveFuture.exceptionally((ex) -> {
            throw new InternalServerErrorException(ex);
        });
    }

    public boolean verifyRefreshToken(String authUserId, String refreshToken) {
        Optional<RefreshToken> dbToken = refreshTokenRepository.findById(refreshToken);
        if (!dbToken.isPresent()) {
            throw new AuthenticationException("Invalid refresh token");
        }
        UUID userId = dbToken.get().getId();
        Date expiry = dbToken.get().getExpiry();
        if (!userId.toString().equals(authUserId)) {
            log.error("Refresh Token ownership is invalid");
            throw new AuthenticationException("Token ownership is invalid");
        }
        if (expiry.before(new Date())) {
            log.error("Refresh token expired");
            throw new AuthenticationException("Refresh token expired");
        }
        return true;
    }
}
