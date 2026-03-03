package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.GymInfoDto;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.service.GymInfoService;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.database.service.VisitService;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.service.EntryCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GymEntryFacadeImplTest {

    @Mock
    UserService userService;
    @Mock
    EntryCodeService entryCodeService;
    @Mock
    SubscriptionService subscriptionService;
    @Mock
    GymInfoService gymInfoService;
    @Mock
    VisitService visitService;

    @InjectMocks
    GymEntryFacadeImpl gymEntryFacade;

    private static final String MOCK_EMAIL = "email@gmail.com";

    @Test
    public void tryEntryTest_withoutThrow() throws NonActiveSubscriptionException, VisitsAreOverException {
        gymEntryFacade.tryEntry(MOCK_EMAIL);

        verify(subscriptionService).validateAndGetActiveSubscription(MOCK_EMAIL);
        verify(entryCodeService).generateUserEntryCode(MOCK_EMAIL);
    }

    @Test
    public void tryEntryTest_throwNonActiveSubscriptionException_withSingleSubscription() throws NonActiveSubscriptionException, VisitsAreOverException {
        when(subscriptionService.validateAndGetActiveSubscription(MOCK_EMAIL)).thenThrow(NonActiveSubscriptionException.class);
        when(userService.findByEmail(MOCK_EMAIL)).thenReturn(Optional.of(UserDto.builder().subscriptions(List.of(new SubscriptionDto())).build()));

        gymEntryFacade.tryEntry(MOCK_EMAIL);

        verify(subscriptionService).validateAndGetActiveSubscription(MOCK_EMAIL);
        verify(subscriptionService).activateSubscription(eq(MOCK_EMAIL), any());
    }

    @Test
    public void confirmEntry() throws NonActiveSubscriptionException, VisitsAreOverException {
        when(subscriptionService.validateAndGetActiveSubscription(any())).thenReturn(SubscriptionDto.builder().visitsTotal(10).tariffType(TariffType.BASIC).visitsLeft(6).build());
        when(userService.findByEmail(MOCK_EMAIL)).thenReturn(Optional.of(UserDto.builder().totalVisits(1).build()));
        when(gymInfoService.getByAddress(any())).thenReturn(Optional.of(new GymInfoDto()));

        gymEntryFacade.confirmEntry("12345", MOCK_EMAIL, "address");

        verify(visitService).create(any());
        verify(userService).update(any());
        verify(subscriptionService).update(any());
    }

    @Test
    public void validateSubscriptionAndDecrementVisitsTest() throws NonActiveSubscriptionException, VisitsAreOverException {
        var code = "123456";
        var email = "email";
        var visitsLeft = 5;
        when(entryCodeService.getEmailByCode(code)).thenReturn(email);
        when(subscriptionService.validateAndGetActiveSubscription(email)).thenReturn(SubscriptionDto.builder().visitsLeft(visitsLeft).build());

        SubscriptionDto sub = gymEntryFacade.validateSubscriptionAndDecrementVisits(code);

        assertEquals(visitsLeft - 1, sub.getVisitsLeft());
    }
}
