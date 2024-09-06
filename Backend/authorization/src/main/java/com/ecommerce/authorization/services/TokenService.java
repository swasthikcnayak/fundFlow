package com.ecommerce.authorization.services;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.dao.RefreshToken;
import com.ecommerce.authorization.dao.projections.AuthInfo;
import com.ecommerce.authorization.dto.JWTResource;
import com.ecommerce.authorization.repository.RefreshTokenRepository;
import com.ecommerce.authorization.utils.errors.AuthenticationException;

import jakarta.ws.rs.InternalServerErrorException;

@Service
public class TokenService {
    
    @Autowired
    RefreshTokenRepository tokenRepository;

    @Autowired
    RedisService redisService;

    public void saveRefreshToken(JWTResource jwtResource, AuthInfo authInfo){
        CompletableFuture<Void> tokenSaveFuture= CompletableFuture.runAsync(()-> {
            RefreshToken token = new RefreshToken(jwtResource.getRefreshToken().toString(), authInfo.getId(), jwtResource.getRefreshTokenExpiry());
            redisService.setValue(jwtResource.getRefreshToken(), jwtResource.getRefreshTokenExpiry());
            tokenRepository.save(token);
         });
         tokenSaveFuture = tokenSaveFuture.exceptionally((ex)->{
            throw new InternalServerErrorException(ex);
         });
    }

    public boolean verifyRefreshToken(String refreshToken){
        Object value = redisService.getValue(refreshToken);
        if(value == null){
            Optional<RefreshToken> dbToken = tokenRepository.findById(refreshToken);
            if(!dbToken.isPresent()){
                return false;
            }
        }
        if(value instanceof Date){
            Date tokenExpiry = (Date)value;
             if(tokenExpiry.before(new Date())){
                throw new AuthenticationException("Token expired");
            }
            return true;
        }
        return false;
    }
}
