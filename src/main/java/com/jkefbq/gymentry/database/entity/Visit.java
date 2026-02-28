package com.jkefbq.gymentry.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "visits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Visit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "gym_address")
    private GymInfo gym;
    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
}
