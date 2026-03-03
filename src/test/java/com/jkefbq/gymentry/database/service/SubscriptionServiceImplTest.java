package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.entity.Subscription;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapperImpl;
import com.jkefbq.gymentry.database.repository.SubscriptionRepository;
import com.jkefbq.gymentry.dto.PeakPurchasesDay;
import com.jkefbq.gymentry.dto.PurchasePerDate;
import com.jkefbq.gymentry.dto.PurchaseTariffTypePerDate;
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
import java.math.RoundingMode;
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
    private static final BigDecimal DEF_SUB_SNAPSHOT_PRICE = BigDecimal.TEN;

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
    public void validateAndGetActiveSubscription_throwNoSuchElementException() {
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> subscriptionService.validateAndGetActiveSubscription(EMAIL));
    }

    @Test
    public void validateAndGetActiveSubscription_throwNonActiveSubscriptionException() {
        UserDto user = getUserDto();
        SubscriptionDto sub = getSubDto(true);
        sub.setActive(false);
        user.setSubscriptions(List.of(sub));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(NonActiveSubscriptionException.class, () -> subscriptionService.validateAndGetActiveSubscription(EMAIL));
    }

    @Test
    public void validateAndGetActiveSubscription_throwVisitsAreOverException() {
        UserDto user = getUserDto();
        SubscriptionDto sub = getSubDto(true);
        sub.setVisitsLeft(0);
        user.setSubscriptions(List.of(sub));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        assertThrows(VisitsAreOverException.class, () -> subscriptionService.validateAndGetActiveSubscription(EMAIL));
    }

    @Test
    public void getAllSubscriptionsTest() {
        var user = getUserDto();
        user.setSubscriptions(List.of(getSubDto(true), getSubDto(false), getSubDto(false)));
        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(user));

        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions(EMAIL);

        assertEquals(user.getSubscriptions().size(), subscriptions.size());
    }

    @Test
    public void activateSubscription_hasNotActiveSubscription() {
        var user = getUserDto();
        var targetSubscription = getSubDto(false);
        var randomSubscription = getSubDto(false);
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
        var targetSubscription = getSubDto(true);
        var randomSubscription = getSubDto(false);
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
    public void getAvgDayCheckTest() {
        var sub = getSubDto(false);
        sub.setSnapshotPrice(BigDecimal.TEN);
        List<SubscriptionDto> subs = List.of(sub, sub, sub);
        var wholeDays = 3L;

        BigDecimal avgDayCheck = subscriptionService.getAvgDayCheck(subs, wholeDays);

        assertEquals(BigDecimal.TEN, avgDayCheck);
    }

    @Test
    public void getPeakDayTest() {
        var target = getSubDto(false);
        target.setPurchaseAt(LocalDate.now().plusDays(3));
        target.setSnapshotPrice(BigDecimal.valueOf(99999999));
        var subs = List.of(target, getSubDto(true), getSubDto(false), getSubDto(false));

        PeakPurchasesDay peak = subscriptionService.getPeakDay(subs);

        assertEquals(target.getSnapshotPrice(), peak.getPurchaseSum());
        assertEquals(target.getPurchaseAt(), peak.getDate());
        assertEquals(1, peak.getPurchaseCount());
    }

    @Test
    public void getPurchasesPerDateTest() {
        var subs = List.of(getSubDto(true), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));
        var totalPrice = DEF_SUB_SNAPSHOT_PRICE.multiply(BigDecimal.valueOf(subs.size()));

        List<PurchasePerDate> purchases = subscriptionService.getPurchasesPerDate(subs);

        assertEquals(1, purchases.size());
        assertEquals(subs.getFirst().getPurchaseAt(), purchases.getFirst().getDate());
        assertEquals(totalPrice, purchases.getFirst().getPurchaseSum());
        assertEquals(subs.size(), purchases.getFirst().getPurchaseCount());
    }

    @Test
    public void getPurchasesPerTariffTest() {
        var subBasic = getSubDto(false);
        subBasic.setTariffType(TariffType.BASIC);
        var subPremium1 = getSubDto(false);
        subPremium1.setTariffType(TariffType.PREMIUM);
        var subPremium2 = getSubDto(false);
        subPremium2.setTariffType(TariffType.PREMIUM);
        var subGod = getSubDto(false);
        subGod.setTariffType(TariffType.GOD);
        var subs = List.of(subPremium1, subPremium2, subBasic, subGod);

        List<PurchaseTariffTypePerDate> purchases = subscriptionService.getPurchasesPerTariff(subs);
        var premiums = purchases.stream().filter(s -> s.getTariffType() == TariffType.PREMIUM).toList();

        assertEquals(subs.size() - 1, purchases.size());
        assertEquals(1, premiums.size());
        assertEquals(2, premiums.getFirst().getPurchaseCount());
        assertEquals(DEF_SUB_SNAPSHOT_PRICE.multiply(BigDecimal.TWO), premiums.getFirst().getPurchaseSum());
    }

    @Test
    public void getTotalRevenueTest() {
        var subs = List.of(getSubDto(true), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));
        var totalPrice = DEF_SUB_SNAPSHOT_PRICE.multiply(BigDecimal.valueOf(subs.size()));

        BigDecimal result = subscriptionService.getTotalRevenue(subs);

        assertEquals(totalPrice, result);
    }

    @Test
    public void getAvgPerPersonTest() {
        var subs = List.of(getSubDto(true), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));
        var totalPrice = DEF_SUB_SNAPSHOT_PRICE.multiply(BigDecimal.valueOf(subs.size()));
        var trueAvgPersonCheck = totalPrice.divide(BigDecimal.valueOf(subs.size()), RoundingMode.HALF_UP);

        BigDecimal resultAvgPersonCheck = subscriptionService.getAvgPerPersonCheck(subs);

        assertEquals(trueAvgPersonCheck, resultAvgPersonCheck);
    }

    @Test
    public void hasActiveSubscriptionsTest_assertFalse() {
        var subs = List.of(getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));

        boolean hasActive = subscriptionService.hasActiveSubscriptions(subs);

        assertFalse(hasActive);
    }

    @Test
    public void hasActiveSubscriptionsTest_assertTrue() {
        var subs = List.of(getSubDto(true), getSubDto(false), getSubDto(false), getSubDto(false), getSubDto(false));

        boolean hasActive = subscriptionService.hasActiveSubscriptions(subs);

        assertTrue(hasActive);
    }

}
