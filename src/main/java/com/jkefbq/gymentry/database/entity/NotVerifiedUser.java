package com.jkefbq.gymentry.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "not_verified_users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotVerifiedUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String firstName;
    @Email
    private String email;
    private String password;
}
