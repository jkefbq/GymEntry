package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.database.service.TariffService;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.facade.GymEntryFacade;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final GymEntryFacade gymEntryFacade;
    private final TariffService tariffService;

    @PostMapping("/confirm-entry")
    public ResponseEntity<@NonNull String> confirmEntry(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody String code) throws VisitsAreOverException, NonActiveSubscriptionException {
        log.info("call /confirm-entry");
        gymEntryFacade.confirmEntry(code, userDetails.getUsername());
        return ResponseEntity.ok("Success! Have a good training session.");
    }

    @PostMapping("/create-tariff")
    public List<TariffDto> createTariff(@RequestBody TariffDto tariffDto) {
        log.info("call /create-tariff");
        tariffService.create(tariffDto);
        return tariffService.getAll();
    }

    @PutMapping("edit/tariffs")
    public List<TariffDto> editTariffs(@RequestBody List<TariffDto> tariffList) {
        log.info("call edit/tariffs");
        return tariffService.saveAll(tariffList);
    }
}
