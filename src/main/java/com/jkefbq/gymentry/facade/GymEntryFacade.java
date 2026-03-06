package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.exception.InvalidSubscriptionException;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;

public interface GymEntryFacade {
    String tryEntry(String email) throws NonActiveSubscriptionException, InvalidSubscriptionException;
    void confirmEntry(String code, String email, String gymAddress) throws NonActiveSubscriptionException, InvalidSubscriptionException;
}
