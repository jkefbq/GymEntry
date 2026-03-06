package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.PartialUserDto;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.database.service.GymInfoService;
import com.jkefbq.gymentry.database.service.SubscriptionManager;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.database.service.VisitManager;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.service.EntryCodeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymEntryFacadeImpl implements GymEntryFacade {

    private final UserService userService;
    private final EntryCodeService entryCodeService;
    private final SubscriptionManager subscriptionManager;
    private final GymInfoService gymInfoService;
    private final VisitManager visitManager;

    @Override
    @Transactional
    public String tryEntry(String email) {
        var userId = userService.findByEmail(email).orElseThrow().getId();
        var subs = subscriptionManager.getUserSubs(userId);
        var activeSub = subscriptionManager.getActiveSubscription(userId);
        checkAllSubs(subs);
        subscriptionManager.checkActiveSub(activeSub);

        return entryCodeService.generateUserEntryCode(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public void confirmEntry(String code, String email, String gymAddress) {
        var activeSub = findAndDecrementSub(code);
        var user = refreshUser(email);
        createVisit(gymAddress, activeSub);

        subscriptionManager.update(activeSub);
        userService.update(user);
    }

    @Transactional
    protected PartialUserDto refreshUser(String email) {
        var user = userService.findByEmail(email).orElseThrow();
        user.setLastVisit(LocalDate.now());
        user.setTotalVisits(user.getTotalVisits() + 1);
        return user;
    }

    @Transactional
    protected SubscriptionDto findAndDecrementSub(String code) {
        var activeSub = findActiveSubscription(code);
        activeSub.setVisitsLeft(activeSub.getVisitsLeft() - 1);
        return activeSub;
    }

    @Transactional
    protected SubscriptionDto findActiveSubscription(String code) {
        var email = entryCodeService.getEmailByCode(code);
        var userId = userService.findByEmail(email).orElseThrow().getId();
        return subscriptionManager.getActiveSubscription(userId);
    }

    @Transactional
    protected void createVisit(String gymAddress, SubscriptionDto activeSub) {
        var gymInfoDto = gymInfoService.getByAddress(gymAddress).orElseThrow();
        visitManager.create(VisitDto.builder()
                .gym(gymInfoDto)
                .createdAt(LocalDateTime.now())
                .subscription(activeSub)
                .build());
    }

    protected void checkAllSubs(List<SubscriptionDto> subs) {
        subs.stream()
                .filter(SubscriptionDto::getActive)
                .reduce((s1, s2) -> {
                    throw new IllegalStateException("user have more then 1 active subscriptions");
                })
                .orElseThrow(NonActiveSubscriptionException::new);
    }

}
