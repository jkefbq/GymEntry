package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.PartialUserDto;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.entity.Subscription;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapperImpl;
import com.jkefbq.gymentry.database.repository.SubscriptionRepository;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

    private static final String EMAIL = "email@gmail.com";
    private static final BigDecimal DEF_SUB_SNAPSHOT_PRICE = BigDecimal.TEN;

    @Mock
    SubscriptionRepository subscriptionRepository;
    @Spy
    SubscriptionMapper subscriptionMapper = new SubscriptionMapperImpl();
    @Mock
    UserService userService;

    @Spy
    @InjectMocks
    SubscriptionService subscriptionService;

    public SubscriptionDto getSubDto(boolean isActive) {
        return SubscriptionDto.builder()
                .id(UUID.randomUUID())
                .active(isActive)
                .snapshotPrice(DEF_SUB_SNAPSHOT_PRICE)
                .tariffType(TariffType.BASIC)
                .visitsLeft(5)
                .visitsTotal(10)
                .purchaseAt(LocalDate.now())
                .build();
    }

    private PartialUserDto getPartialUser() {
        return PartialUserDto.builder()
                .id(UUID.randomUUID())
                .role(UserRole.USER)
                .email(EMAIL)
                .firstName("firstname")
                .totalVisits(1)
                .lastVisit(LocalDate.now())
                .memberSince(LocalDate.now())
                .build();
    }

    @Test
    public void createTest_notLastVisit_assertActive() {
        Subscription entity = subscriptionMapper.toEntity(getSubDto(true));
        when(subscriptionRepository.save(any())).thenReturn(entity);
        var sub = subscriptionService.create(getSubDto(true));

        verify(subscriptionRepository).save(any());
        assertTrue(sub.getActive());
    }

    @Test
    public void createTest_lastVisit_assertNotActive() {
        SubscriptionDto sub = getSubDto(true);
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
        Subscription entity = subscriptionMapper.toEntity(getSubDto(true));
        when(subscriptionRepository.save(any())).thenReturn(entity);

        var sub = subscriptionService.create(getSubDto(true));

        verify(subscriptionRepository).save(any());
        assertTrue(sub.getActive());
    }

    @Test
    public void validateAndGetActiveSubscription_throwNonActiveSubscriptionException() {
        doAnswer(invocation ->
            List.of(getSubDto(false), getSubDto(false), getSubDto(false))
        ).when(subscriptionService).getUserSubs(any());
        var userId = UUID.randomUUID();

        assertThrows(NonActiveSubscriptionException.class, () -> subscriptionService.getActiveSubscription(userId));
    }

    @Test
    public void validateAndGetActiveSubscription_throwIllegalStateException() {
        doAnswer(invocation ->
                List.of(getSubDto(true), getSubDto(true), getSubDto(false))
        ).when(subscriptionService).getUserSubs(any());
        var userId = UUID.randomUUID();

        assertThrows(IllegalStateException.class, () -> subscriptionService.getActiveSubscription(userId));
    }

    @Test
    public void activateSubscriptionTest() {
        doAnswer(invocation -> Optional.of(getSubDto(false)))
                .when(subscriptionService).findById(any());
        doAnswer(invocation -> invocation.getArgument(0))
                .when(subscriptionService).update(any());

        SubscriptionDto sub = subscriptionService.activateSubscription(UUID.randomUUID());

        assertTrue(sub.getActive());
    }

    @Test
    public void getAllForPeriodTest() {
        var from = LocalDate.now();
        var to = LocalDate.now();

        subscriptionService.getAllForPeriod(from, to);

        verify(subscriptionRepository).getAllForPeriod(from, to);
    }

    @Test
    public void hasActiveSubscriptionsTest_assertFalse() {
        var subs = List.of(getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));

        boolean hasActive = subscriptionService.hasActiveSubscription(subs);

        assertFalse(hasActive);
    }

    @Test
    public void hasActiveSubscriptionsTest_assertTrue() {
        var subs = List.of(getSubDto(true), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));

        boolean hasActive = subscriptionService.hasActiveSubscription(subs);

        assertTrue(hasActive);
    }

    @Test
    public void findByIdTest() {
        subscriptionService.findById(UUID.randomUUID());
        verify(subscriptionRepository).findById(any());
    }

    @Test
    public void deactivateSubscriptionTest() {
        var sub = getSubDto(true);
        when(subscriptionService.findById(sub.getId())).thenReturn(Optional.of(sub));
        doAnswer(invocation -> invocation.getArgument(0)).when(subscriptionService).update(any());
        var updSub = subscriptionService.deactivateSubscription(sub.getId());

        assertFalse(updSub.getActive());
    }

}
