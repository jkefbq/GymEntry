package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.database.dto.SubscriptionRequestDto;
import com.jkefbq.gymentry.database.dto.SubscriptionResponseDto;
import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.database.service.TariffService;
import com.jkefbq.gymentry.facade.MarketFacade;
import com.jkefbq.gymentry.database.dto.TariffType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/market")
@RequiredArgsConstructor
public class PurchaseController {

    private final TariffService tariffService;
    private final MarketFacade marketFacade;

    @GetMapping
    public ResponseEntity<@NonNull List<TariffDto>> getAllTariffs() {
        log.info("call /market");
        return ResponseEntity.ok(tariffService.getAll());
    }

    @GetMapping("/calculate-price/{tariff-type}/{visits}")
    public ResponseEntity<@NonNull BigDecimal> calculatePrice(
            @PathVariable("tariff-type") TariffType tariffType,
            @PathVariable("visits") Integer visitsCount
    ) {
        log.info("call /market/calculate-price/{}/{}", tariffType, visitsCount);
        BigDecimal price = marketFacade.calculatePrice(tariffType, visitsCount);
        return ResponseEntity.ok(price);
    }

    @PostMapping("/subscription")
    public ResponseEntity<@NonNull SubscriptionResponseDto> create(
            @RequestBody SubscriptionRequestDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("call /market/subscription");
        SubscriptionResponseDto response = marketFacade.create(requestDto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}