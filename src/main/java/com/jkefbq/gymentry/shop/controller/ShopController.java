package com.jkefbq.gymentry.shop.controller;

import com.jkefbq.gymentry.shop.dto.TicketDo;
import com.jkefbq.gymentry.shop.dto.TicketEvent;
import com.jkefbq.gymentry.shop.dto.Ticket;
import com.jkefbq.gymentry.shop.service.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/shop")
@AllArgsConstructor
public class ShopController {

    private final TicketService ticketService;

    @PostMapping("/onetime")
    public void buyOneTimeTraining(@RequestBody Ticket ticket) {
        log.info("принял");
        val event = new TicketEvent(ticket, TicketDo.BUY);
        ticketService.buyTicket(event);
    }

}
