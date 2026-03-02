package com.jkefbq.gymentry.controller;

import com.jkefbq.gymentry.dto.EntryCode;
import com.jkefbq.gymentry.database.dto.GymInfoDto;
import com.jkefbq.gymentry.dto.PurchaseStatistics;
import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.dto.VisitStatistics;
import com.jkefbq.gymentry.database.service.GymInfoService;
import com.jkefbq.gymentry.database.service.TariffService;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import com.jkefbq.gymentry.facade.AdminStatisticsFacade;
import com.jkefbq.gymentry.facade.GymEntryFacade;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final GymEntryFacade gymEntryFacade;
    private final TariffService tariffService;
    private final AdminStatisticsFacade adminStatisticsFacade;
    private final GymInfoService gymInfoService;

    @PostMapping("/confirm-entry")
    public ResponseEntity<@NonNull String> confirmEntry(
            @AuthenticationPrincipal UserDetails userDetails, @RequestBody @Valid EntryCode code, @RequestParam String gymAddress) throws VisitsAreOverException, NonActiveSubscriptionException {
        log.info("call /admin/confirm-entry");
        gymEntryFacade.confirmEntry(code.getCode(), userDetails.getUsername(), gymAddress);
        return ResponseEntity.ok("Успех! посетитель может идти на тренировку");
    }

    @PostMapping("/create/tariff")
    public List<TariffDto> createTariff(@RequestBody TariffDto tariffDto) {
        log.info("call /admin/create-tariff");
        tariffService.create(tariffDto);
        return tariffService.getAll();
    }

    @PutMapping("edit/tariffs")
    public List<TariffDto> editTariffs(@RequestBody List<TariffDto> tariffList) {
        log.info("call /admin/edit/tariffs");
        return tariffService.saveAll(tariffList);
    }

    @GetMapping("/tariffs/types")
    public TariffType[] getAllTariffTypes() {
        log.info("call /admin/tariff-types");
        return TariffType.values();
    }

    @DeleteMapping("/delete/tariffs")
    public ResponseEntity<@NonNull String> deleteTariffs(@RequestBody List<TariffDto> tariffList) {
        log.info("call /admin/delete-tariffs with args {}", tariffList);
        tariffService.deleteAll(tariffList);
        return ResponseEntity.ok("success");
    }

    @PutMapping("/edit/gym-info")
    public GymInfoDto editGymInfoDto(@RequestBody GymInfoDto gymInfoDto) {
        log.info("call /admin/edit/gym-info");
        return gymInfoService.save(gymInfoDto);
    }

    @GetMapping("/gym/addresses")
    public List<String> getAllAddresses() {
        log.info("call /admin/gym/addresses");
        return gymInfoService.getAllAddresses();
    }

    @GetMapping("/statistics/visits")
    public VisitStatistics getVisitStatisticsForPeriod(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to, @RequestParam String gymAddress) {
        log.info("call /admin/statistics/visits/summary?from={}&to={}&gymAddress={}", from, to, gymAddress);
        return adminStatisticsFacade.getVisitStatisticsForPeriod(from, to, gymAddress);
    }

    @GetMapping("/statistics/purchases")
    public PurchaseStatistics getPurchaseStatisticsForPeriod(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        log.info("call /admin/statistics/purchases/summary?from={}&to={}", from, to);
        return adminStatisticsFacade.getPurchaseStatisticsForPeriod(from, to);
    }

}