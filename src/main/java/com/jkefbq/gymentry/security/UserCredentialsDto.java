package com.jkefbq.gymentry.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCredentialsDto {
    @Email
    private String email;
    @Size(min = 5, max = 255)
    private String password;
}