package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.entity.Subscription;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapperImpl;
import com.jkefbq.gymentry.database.repository.SubscriptionRepository;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.security.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    @Mock
    SubscriptionRepository subscriptionRepository;
    @Spy
    SubscriptionMapper subscriptionMapper = new SubscriptionMapperImpl();
    @Mock
    UserService userService;

    @Spy
    @InjectMocks
    SubscriptionServiceImpl subscriptionService;

    private static final String EMAIL = "email@gmail.com";

    public SubscriptionDto getSubscriptionDto(boolean isActive) {
        return SubscriptionDto.builder()
                .id(UUID.randomUUID())
                .active(isActive)
                .snapshotPrice(BigDecimal.TEN)
                .tariffType(TariffType.BASIC)
                .visitsLeft(5)
                .visitsTotal(10)
                .purchaseAt(LocalDate.now())
                .build();
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .id(UUID.randomUUID())
                .role(UserRole.USER)
                .email(EMAIL)
                .firstName("firstname")
                .password("password")
                .totalVisits(1)
                .lastVisit(LocalDate.now())
                .memberSince(LocalDate.now())
                .build();
    }

    @Test
    public void createTest_notLastVisit_assertActive() {
        Subscription entity = subscriptionMapper.toEntity(getSubscriptionDto(true));
        when(subscriptionRepository.save(any())).thenReturn(entity);
        var sub = subscriptionService.create(getSubscriptionDto(true));

        verify(subscriptionRepository).save(any());
        assertTrue(sub.getActive());
    }

    @Test
    public void createTest_lastVisit_assertNotActive() {
        SubscriptionDto sub = getSubscriptionDto(true);
        sub.setVisitsLeft(0);
        doAnswer(invocation ->
                invocation.getArgument(0)
        ).when(subscriptionRepository).save(any());


        var savedSub = subscriptionService.create(sub);

        verify(subscriptionRepository).save(any());
        assertFalse(savedSub.getActive());
    }

    @Test
    public void updateTest() {
        Subscription entity = subscriptionMapper.toEntity(getSubscriptionDto(true));
        when(subscriptionRepository.save(any())).thenReturn(entity);

        var sub = subscriptionService.create(getSubscriptionDto(true));

        verify(subscriptionRepository).save(any());
        assertTrue(sub.getActive());
    }

    @Test
    public void validateAndGetActiveSubscription_throwNoSuchElementException() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> subscriptionService.validateAndGetActiveSubscription(EMAIL));
    }

    @Test
    public void validateAndGetActiveSubscription_throwNonActiveSubscriptionException() {
        UserDto user = getUserDto();
        SubscriptionDto sub = getSubscriptionDto(true);
        sub.setActive(false);
        user.setSubscriptions(List.of(sub));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(NonActiveSubscriptionException.class, () -> subscriptionService.validateAndGetActiveSubscription(EMAIL));
    }

    @Test
    public void validateAndGetActiveSubscription_throwVisitsAreOverException() {
        UserDto user = getUserDto();
        SubscriptionDto sub = getSubscriptionDto(true);
        sub.setVisitsLeft(0);
        user.setSubscriptions(List.of(sub));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(VisitsAreOverException.class, () -> subscriptionService.validateAndGetActiveSubscription(EMAIL));
    }

    @Test
    public void getAllSubscriptionsTest() {
        var user = getUserDto();
        user.setSubscriptions(List.of(getSubscriptionDto(true), getSubscriptionDto(false), getSubscriptionDto(false)));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions(EMAIL);

        assertEquals(3, subscriptions.size());
    }

    @Test
    public void activateSubscription_hasNotActiveSubscription() {
        var user = getUserDto();
        var targetSubscription = getSubscriptionDto(false);
        var randomSubscription = getSubscriptionDto(false);
        var targetSubscriptionId = UUID.randomUUID();
        targetSubscription.setId(targetSubscriptionId);
        user.setSubscriptions(List.of(targetSubscription, randomSubscription));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        doAnswer(invocation ->
                invocation.getArgument(0)).when(subscriptionService).update(any());

        SubscriptionDto sub = subscriptionService.activateSubscription(EMAIL, targetSubscriptionId);

        assertTrue(sub.getActive());
    }

    @Test
    public void activateSubscription_hasActiveSubscription() {
        var user = getUserDto();
        var targetSubscription = getSubscriptionDto(true);
        var randomSubscription = getSubscriptionDto(false);
        var targetSubscriptionId = UUID.randomUUID();
        targetSubscription.setId(targetSubscriptionId);
        user.setSubscriptions(List.of(targetSubscription, randomSubscription));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class, () ->
                subscriptionService.activateSubscription(EMAIL, targetSubscriptionId));
    }

    @Test
    public void getAllForPeriodTest() {
        var from = LocalDate.now();
        var to = LocalDate.now();

        subscriptionService.getAllForPeriod(from, to);

        verify(subscriptionRepository).getAllForPeriod(from, to);
    }

    @Test
    public void getAvgDayCheck() {
        var sub = getSubscriptionDto(false);
        sub.setSnapshotPrice(BigDecimal.TEN);
        List<SubscriptionDto> subscriptions = List.of(sub, sub, sub);
        var wholeDays = 3L;

        BigDecimal avgDayCheck = subscriptionService.getAvgDayCheck(subscriptions, wholeDays);

        assertEquals(BigDecimal.TEN, avgDayCheck);
    }

}
