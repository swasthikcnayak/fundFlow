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
import com.ecommerce.authorization.dto.response.LoginResource;
import com.ecommerce.authorization.dto.response.RefreshResponse;
import com.ecommerce.authorization.dto.response.TokenResource;
import com.ecommerce.authorization.repository.UserRepository;
import com.ecommerce.authorization.utils.ObjectUtils;
import com.ecommerce.authorization.utils.errors.AuthenticationException;
import com.ecommerce.authorization.utils.errors.BadRequestException;
import io.jsonwebtoken.Claims;

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
            .isVerified(false)
            .lastLoggedIn(null)
            .build();
        try{ 
            user = this.userRepository.save(user);
        }catch(Exception e){
            throw new BadRequestException("Account already exists");
        }
    }

    public LoginResource login(UserLoginRequest userLoginRequest) throws AuthenticationException{
        AuthInfo auth = userRepository.findOneByEmailAndIsVerified(userLoginRequest.getEmail(), true);
        if(auth == null){
            throw new AuthenticationException("Wrong email/password or email not verified");
        }
        boolean isValid = secretService.comparePassword(userLoginRequest.getPassword(), auth.getPassword());
        if(isValid == false){
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
                throw new AuthenticationException("Expired token");
            }
            return tokenResource;
        }
        throw new BadRequestException("Invalid token");
    }

    public RefreshResponse refreshToken(String token, String refreshToken) throws BadRequestException, AuthenticationException {
        TokenResource tokenResource = this.validateToken(token);
        if(tokenService.verifyRefreshToken(tokenResource.getId(), refreshToken)){
            Map<String, Object> claims = objectUtils.getClaims(tokenResource.getEmail());
            JWTResource jwtResource = secretService.generateToken(tokenResource.getId(), claims);
            return new RefreshResponse(tokenResource.getId(), jwtResource.getToken());
        }
        throw new AuthenticationException("Invalid refresh token");
    }

}
