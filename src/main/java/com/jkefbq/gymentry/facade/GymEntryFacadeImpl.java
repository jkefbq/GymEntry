package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.exception.SubscriptionExpiredException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GymEntryFacadeImpl implements GymEntryFacade {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Override
    @Transactional
    public void entry(String email) throws VisitsAreOverException, SubscriptionExpiredException {
        SubscriptionDto subscription = userService.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("user with email " + email + " not found"))
                .getSubscriptions().stream()
                .filter(SubscriptionDto::isActive)
                .findFirst()
                .orElseThrow(() -> new SubscriptionExpiredException("you do not have any active subscriptions"));
        decrementVisitsAndRefresh(subscription);
    }

    @Transactional
    protected void decrementVisitsAndRefresh(SubscriptionDto subscription) throws VisitsAreOverException {
        if (subscription.getVisitsLeft() <= 0) {
            throw new VisitsAreOverException("you have used up all your visits in your subscription");
        }
        subscription.setVisitsLeft(subscription.getVisitsLeft() - 1);
        refreshSubscriptionData(subscription);
        subscriptionService.update(subscription);
    }

    private void refreshSubscriptionData(SubscriptionDto subscriptionDto) {
        if (subscriptionDto.getVisitsLeft() <= 0) {
            subscriptionDto.setActive(false);
        }
    }
}
