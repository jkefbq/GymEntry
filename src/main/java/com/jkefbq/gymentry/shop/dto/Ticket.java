package com.jkefbq.gymentry.shop.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Ticket {
    private UUID ticketId;
    private UUID userId;
    private Integer workoutCount;
    private LocalDate purchasedAt;
}
