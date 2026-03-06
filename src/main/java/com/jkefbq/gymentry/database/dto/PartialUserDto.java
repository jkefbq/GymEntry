package com.jkefbq.gymentry.database.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jkefbq.gymentry.security.UserRole;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class PartialUserDto implements Serializable {
    private UUID id;
    private String firstName;
    @Email
    private String email;
    private UserRole role;
    private Integer totalVisits;
    private LocalDate memberSince;
    private LocalDate lastVisit;
}
