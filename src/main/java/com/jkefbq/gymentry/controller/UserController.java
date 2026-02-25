package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @GetMapping("me")
    public ResponseEntity<@NonNull UserDto> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("call user/me, user with email {}", userDetails.getUsername());
        UserDto user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("user with email " + userDetails.getUsername() + " not found"));
        return ResponseEntity.ok(user);
    }

    @GetMapping("subscriptions")
    public ResponseEntity<@NonNull List<SubscriptionDto>> getAllSubscriptions(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("call user/subscriptions, user with email {}", userDetails.getUsername());
        var subscriptions = subscriptionService.getAllSubscriptions(userDetails.getUsername());
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("subscriptions/active")
    public ResponseEntity<@NonNull SubscriptionDto> getActiveSubscription(@AuthenticationPrincipal UserDetails userDetails) throws VisitsAreOverException, NonActiveSubscriptionException {
        log.info("call user/subscriptions/active, user with email {}", userDetails.getUsername());
        var subscription = subscriptionService.validateAndGetActiveSubscription(userDetails.getUsername());
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("subscriptions/activate")
    public ResponseEntity<@NonNull SubscriptionDto> activateSubscription(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody UUID subscriptionId) {
        log.info("call user/subscriptions/activate, user with email {}", userDetails.getUsername());
        var activatedSubscription = subscriptionService.activateSubscription(userDetails.getUsername(), subscriptionId);
        return ResponseEntity.ok(activatedSubscription);
    }

}