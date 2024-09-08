package com.fundflow.authorization.services;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundflow.authorization.dao.User;
import com.fundflow.authorization.dao.projections.AuthInfo;
import com.fundflow.authorization.dto.JWTResource;
import com.fundflow.authorization.dto.request.UserLoginRequest;
import com.fundflow.authorization.dto.request.UserRegistrationRequest;
import com.fundflow.authorization.dto.response.LoginResource;
import com.fundflow.authorization.dto.response.RefreshResponse;
import com.fundflow.authorization.dto.response.TokenResource;
import com.fundflow.authorization.repository.UserRepository;
import com.fundflow.authorization.utils.ObjectUtils;
import com.fundflow.authorization.utils.errors.AuthenticationException;
import com.fundflow.authorization.utils.errors.BadRequestException;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private SecretService secretService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectUtils objectUtils;

    @Autowired
    private TokenService tokenService;

    public void register(UserRegistrationRequest registrationRequest) throws BadRequestException {
        String passwordHash = this.secretService.getPasswordHash(registrationRequest.getPassword());
        User user = User.builder()
            .email(registrationRequest.getEmail().toLowerCase())
            .password(passwordHash)
            .build();
        try{ 
            user = this.userRepository.save(user);
        }catch(Exception e){
            log.error("Account already exists", e);
            throw new BadRequestException("Account already exists");
        }
    }

    public LoginResource login(UserLoginRequest userLoginRequest) throws AuthenticationException{
        AuthInfo auth = userRepository.findOneByEmailAndIsVerified(userLoginRequest.getEmail(), true);
        if(auth == null){
            log.warn("AuthInfo not found");
            throw new AuthenticationException("Wrong email/password or email not verified");
        }
        boolean isValid = secretService.comparePassword(userLoginRequest.getPassword(), auth.getPassword());
        if(isValid == false){
            log.warn("Password mismatch");
            throw new AuthenticationException("Wrong email/password");
        }
        Map<String, Object> claims = objectUtils.getClaims(auth.getEmail());
        JWTResource jwtResource = secretService.generateToken(auth.getId().toString(),claims);
        tokenService.saveRefreshToken(jwtResource, auth);
        return new LoginResource(auth.getEmail(), jwtResource.getToken(), jwtResource.getRefreshToken().toString());
    }

    public TokenResource validateToken(String token) throws BadRequestException, AuthenticationException {
        token = objectUtils.preprocessToken(token);
        Object payload = secretService.extractJwt(token);
        if (payload instanceof Claims) {
            TokenResource tokenResource = objectUtils.getTokenResource(objectUtils.getMapFromClaims((Claims)payload));
            if(tokenResource.getExpiry().before(new Date())){
                log.error("Expired taken expiry date: "+tokenResource.getExpiry());
                throw new AuthenticationException("Expired token");
            }
            return tokenResource;
        }
        log.warn("Token is invalid");
        throw new BadRequestException("Invalid token");
    }

    public RefreshResponse refreshToken(String token, String refreshToken) throws BadRequestException, AuthenticationException {
        TokenResource tokenResource = this.validateToken(token);
        if(tokenService.verifyRefreshToken(tokenResource.getId(), refreshToken)){
            Map<String, Object> claims = objectUtils.getClaims(tokenResource.getEmail());
            JWTResource jwtResource = secretService.generateToken(tokenResource.getId(), claims);
            return new RefreshResponse(tokenResource.getId(), jwtResource.getToken());
        }
        log.warn("Token is invalid");
        throw new AuthenticationException("Invalid refresh token");
    }

}
