package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.exception.SubscriptionExpiredException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;

public interface GymEntryFacade {
    void entry(String email) throws VisitsAreOverException, SubscriptionExpiredException;
}
