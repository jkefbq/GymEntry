package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.facade.GymEntryFacade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EntryController {

    private final GymEntryFacade gymEntryFacade;

    @PutMapping("/entry")
    public ResponseEntity<@NonNull String> getGymEntryCode(@AuthenticationPrincipal UserDetails userDetails) throws VisitsAreOverException, NonActiveSubscriptionException {
        log.info("call /entry for user with email {}", userDetails.getUsername());
        String entryCode = gymEntryFacade.tryEntry(userDetails.getUsername());
        return ResponseEntity.ok(entryCode);
    }
}
