package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.exception.SubscriptionExpiredException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.facade.GymEntryFacade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EntryController {

    private final GymEntryFacade gymEntryFacade;

    @GetMapping("/entry")//когда отсканил qr
    public ResponseEntity<@NonNull String> getGymEntryCode(@AuthenticationPrincipal UserDetails userDetails) throws VisitsAreOverException, SubscriptionExpiredException {
        gymEntryFacade.entry(userDetails.getUsername());
        return ResponseEntity.ok("Success! Have a great workout!");
    }
}
