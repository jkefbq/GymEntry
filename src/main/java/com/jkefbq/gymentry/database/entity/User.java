package com.jkefbq.gymentry.database.entity;

import com.jkefbq.gymentry.security.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String firstName;
    @Email
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private Integer totalVisits;
    private LocalDate memberSince;
    private LocalDate lastVisit;
    @OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions;
}
