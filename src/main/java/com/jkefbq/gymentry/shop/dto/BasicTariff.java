//package com.jkefbq.gymentry.shop.dto;
//
//import com.jkefbq.gymentry.entity.Tariff;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.math.BigDecimal;
//
//public class BasicTariff extends Tariff {
//
//    @Value("${app.tariff.basic.price-per-workout}")
//    private BigDecimal pricePerWorkout;
//
//    public BasicTariff(String tariffName, Ticket ticket) {
//        super(tariffName, ticket);
//    }
//
//    @Override
//    public BigDecimal calculateTariffPrice(Integer workoutCount) {
//        return pricePerWorkout.multiply(
//                BigDecimal.valueOf(workoutCount)
//        );
//    }
//}
