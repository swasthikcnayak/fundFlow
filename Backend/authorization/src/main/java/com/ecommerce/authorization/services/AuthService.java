package com.ecommerce.authorization.services;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.authorization.dao.User;
import com.ecommerce.authorization.dao.projections.AuthInfo;
import com.ecommerce.authorization.dto.JWTResource;
import com.ecommerce.authorization.dto.request.UserLoginRequest;
import com.ecommerce.authorization.dto.request.UserRegistrationRequest;
import com.ecommerce.authorization.dto.response.AuthResource;
import com.ecommerce.authorization.dto.response.TokenResource;
import com.ecommerce.authorization.repository.UserRepository;
import com.ecommerce.authorization.utils.ObjectUtils;
import com.ecommerce.authorization.utils.errors.AuthenticationException;
import com.ecommerce.authorization.utils.errors.BadRequestException;
import io.jsonwebtoken.Claims;

@Service
public class AuthService {

    @Autowired
    SecretService secretService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectUtils objectUtils;

    @Autowired
    TokenService tokenService;

    public void register(UserRegistrationRequest registrationRequest) throws BadRequestException {
        String passwordHash = this.secretService.getPasswordHash(registrationRequest.getPassword());
        User user = User.builder()
            .email(registrationRequest.getEmail().toLowerCase())
            .password(passwordHash)
            .isVerified(false)
            .lastLoggedIn(null)
            .build();
        try{ 
            user = this.userRepository.save(user);
        }catch(Exception e){
            throw new BadRequestException("Account already exists");
        }
    }

    public AuthResource login(UserLoginRequest userLoginRequest) throws AuthenticationException{
        AuthInfo auth = userRepository.findOneByEmailAndIsVerified(userLoginRequest.getEmail(), true);
        if(auth == null){
            throw new AuthenticationException("Wrong email/password or email not verified");
        }
        boolean isValid = secretService.comparePassword(userLoginRequest.getPassword(), auth.getPassword());
        if(isValid == false){
            throw new AuthenticationException("Wrong email/password");
        }
        Map<String, Object> claims = objectUtils.getClaims(auth);
        JWTResource jwtResource = secretService.generateToken(auth.getId().toString(),claims);
        tokenService.saveToken(jwtResource, auth);
        return new AuthResource(auth.getEmail(), jwtResource.getToken());
    }

    public TokenResource validateToken(String token) throws BadRequestException, AuthenticationException {
        token = objectUtils.preprocessToken(token);
        if(!tokenService.verify(token)){
            throw new AuthenticationException("Invalid token");
        }
        Object payload = secretService.extractJwt(token);
        if (payload instanceof Claims) {
            TokenResource tokenResource = objectUtils.getTokenResource(objectUtils.getMapFromClaims((Claims)payload));
            if(tokenResource.getExpiry().before(new Date())){
                throw new AuthenticationException("Expired token");
            }
            return tokenResource;
        }
        throw new BadRequestException("Invalid token");
    }

}
