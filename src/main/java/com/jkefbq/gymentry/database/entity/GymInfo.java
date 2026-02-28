package com.jkefbq.gymentry.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gym_info")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GymInfo {
    @Id
    private String address;
}
