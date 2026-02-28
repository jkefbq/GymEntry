package com.jkefbq.gymentry.kafka;

import com.jkefbq.gymentry.database.dto.VisitDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VisitEvent {
    private final VisitDto visit;
}
