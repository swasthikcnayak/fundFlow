package com.ecommerce.authorization.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.Data;

@Data
public class UserRegistrationRequest{

    @NotNull(message = "The email is required")
    @NotEmpty(message = "The email address should not be empty")
    @Email(message = "The email address is invalid.", flags = { Flag.CASE_INSENSITIVE })
    String email;

    @NotNull(message = "The password is required")
    @NotEmpty(message = "Password should not be empty")
    @Size(min=8, max=16)
    String password;
}