package com.jkefbq.gymentry.database.dto;

import com.jkefbq.gymentry.security.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String firstName;
    @Email
    private String email;
    @Size(min = 5, max = 255)
    private String password;
    private UserRole authority;
}
