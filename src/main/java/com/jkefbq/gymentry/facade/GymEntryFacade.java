package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;

public interface GymEntryFacade {
    String tryEntry(String email) throws VisitsAreOverException, NonActiveSubscriptionException;
    void confirmEntry(String code, String email) throws NonActiveSubscriptionException, VisitsAreOverException;
}
