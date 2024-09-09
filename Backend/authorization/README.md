# Authorization Microservice

This microservice is built in spring boot and is responsible for handling all the authentication related aspects of the system.

## Controllers

This Microservice has only one controller [AuthController](src/main/java/com/fundflow/authorization/controllers/AuthController.java)

Root path of the controller -> "/auth/v1/"

## Services

The microservice uses different services for managing different request routes and internal functions.

1. [AuthService](src/main/java/com/fundflow/authorization/services/AuthService.java) - This service is entry point for login, register, validating the token, creating tokens etc.
2. [SecretService](src/main/java/com/fundflow/authorization/services/SecretService.java) - This is a helper service that maintains the application secrets like jwt secret key, expiration, and is internally responsible for creation of tokens. The responsibility is delegated to this service by Authservice.
3. [TokenService](src/main/java/com/fundflow/authorization/services/TokenService.java) - This service is responsible for token management, saving token, fetching tokens.
4. [VerificationService](src/main/java/com/fundflow/authorization/services/VerificationService.java) - This service responsible for verification of user.

## Database tables

1. refresh_token
   This table stores the information on the refresh token that are created and handed to the clients. 
   
2. users
    This table stores the information of the users.

3. verification_token
    This stores the information of the verification tokens that are created and handed over to the clients during registration.
   