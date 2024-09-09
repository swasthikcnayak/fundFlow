package com.fundflow.authorization.services;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.fundflow.authorization.dao.User;
import com.fundflow.authorization.dao.VerificationToken;
import com.fundflow.authorization.repository.UserRepository;
import com.fundflow.authorization.repository.VerificationTokenRepository;
import com.fundflow.authorization.utils.Constants;
import com.fundflow.authorization.utils.errors.BadRequestException;
import com.fundflow.authorization.utils.errors.NotFoundException;

import io.micrometer.common.util.StringUtils;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerificationService {

    VerificationTokenRepository verificationTokenRepository;

    UserRepository userRepository;

    Long verificationExpiration;

    public VerificationService(Environment environment, VerificationTokenRepository verificationTokenRepository,
            UserRepository userRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.verificationExpiration = Long
                .parseLong(Objects.requireNonNull(environment.getProperty(Constants.VERFICATION_EXPIRY)));
    }

    public void triggerVerification(User user) {
        CompletableFuture<Void> verificationTriggerFuture = CompletableFuture.runAsync(() -> {
            Date expiry = new Date(new Date().getTime() + this.verificationExpiration);
            VerificationToken token = VerificationToken.builder().userId(user.getId()).expiry(expiry).build();
            token = verificationTokenRepository.save(token);
            // TODO @SWASTHIK: SEND EMAIL
        });
        verificationTriggerFuture.exceptionally(ex -> {
            log.error("Failed to save refresh token", ex);
            throw new InternalServerErrorException(ex);
        });
    }

    public void verifyUser(String token) {
        if (StringUtils.isEmpty(token)) {
            log.warn("Invalid verification token " + token);
            throw new BadRequestException("Invalid verificaiton token");
        }
        Optional<VerificationToken> vToken = this.verificationTokenRepository.findById(token);
        if (!vToken.isPresent()) {
            log.error("Verification token not found in db " + token);
            throw new BadRequestException("Invalid verfication token");
        }

        Optional<User> user = userRepository.findById(vToken.get().getUserId());
        if (!user.isPresent()) {
            log.error("User not found in db " + vToken.get().getUserId());
            throw new NotFoundException("User not found");
        }
        user.get().setIsVerified(true);
        userRepository.save(user.get());
    }

}
