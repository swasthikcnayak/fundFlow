package com.ecommerce.authorization.services;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.dao.Token;
import com.ecommerce.authorization.dao.projections.AuthInfo;
import com.ecommerce.authorization.dto.JWTResource;
import com.ecommerce.authorization.repository.TokenRepository;
import com.ecommerce.authorization.utils.errors.AuthenticationException;

import jakarta.ws.rs.InternalServerErrorException;

@Service
public class TokenService {
    
    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    RedisService redisService;

    public void saveToken(JWTResource jwtResource, AuthInfo authInfo){
        CompletableFuture<Void> tokenSaveFuture= CompletableFuture.runAsync(()-> {
            Token token = Token.builder().expiry(jwtResource.getExpiry()).token(jwtResource.getToken()).id(authInfo.getId()).build();
            redisService.setValue(jwtResource.getToken(), jwtResource.getExpiry());
            tokenRepository.save(token);
         });
         tokenSaveFuture = tokenSaveFuture.exceptionally((ex)->{
            throw new InternalServerErrorException(ex);
         });
    }

    public boolean verify(String token){
        Object value = redisService.getValue(token);
        if(value == null){
            Optional<Token> dbToken = tokenRepository.findById(token);
            if(!dbToken.isPresent()){
                return false;
            }
        }
        if(value instanceof Date){
            Date tokenExpiry = (Date)value;
             if(tokenExpiry.before(new Date())){
                throw new AuthenticationException("Expired token");
            }
            return true;
        }
        return false;
    }
}
