package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.service.EntryCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GymEntryFacadeImpl implements GymEntryFacade {

    private final UserService userService;
    private final EntryCodeService entryCodeService;
    private final SubscriptionService subscriptionService;

    @Override
    @Transactional
    public String tryEntry(String email) throws VisitsAreOverException, NonActiveSubscriptionException {
        try {
            subscriptionService.validateAndGetActiveSubscription(email);
        } catch (NonActiveSubscriptionException e) {
            UserDto user = userService.findByEmail(email).orElseThrow();
            long subscriptionCount = user.getSubscriptions().size();
            if (subscriptionCount == 1) {
                subscriptionService.activateSubscription(email, user.getSubscriptions().getFirst().getId());
            } else {
                throw e;
            }
        }
        return entryCodeService.generateUserEntryCode(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void confirmEntry(String code, String email) throws NonActiveSubscriptionException, VisitsAreOverException {
        SubscriptionDto subscription = validateSubscriptionAndDecrementVisits(code);
        UserDto user = userService.findByEmail(email).orElseThrow();
        user.setLastVisit(LocalDate.now());
        user.setTotalVisits(user.getTotalVisits() + 1);
        userService.update(user);
        subscriptionService.update(subscription);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    protected SubscriptionDto validateSubscriptionAndDecrementVisits(String code) throws NonActiveSubscriptionException, VisitsAreOverException {
        String email = entryCodeService.getEmailByCode(code);
        SubscriptionDto subscription = subscriptionService.validateAndGetActiveSubscription(email);
        subscription.setVisitsLeft(subscription.getVisitsLeft() - 1);
        return subscription;
    }
}
