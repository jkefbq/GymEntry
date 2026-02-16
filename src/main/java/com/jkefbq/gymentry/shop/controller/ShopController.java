package com.jkefbq.gymentry.shop.controller;

import com.jkefbq.gymentry.shop.dto.BasicTariff;
import com.jkefbq.gymentry.shop.dto.Ticket;
import com.jkefbq.gymentry.shop.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shop")
@AllArgsConstructor
public class ShopController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/tariffs")
    public List<Ticket> getAllTariffs() {
        BasicTariff basicTariff = new BasicTariff("", new Ticket());
    }

}
