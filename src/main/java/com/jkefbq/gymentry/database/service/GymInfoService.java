package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.GymInfoDto;

import java.util.List;
import java.util.Optional;

public interface GymInfoService {
    GymInfoDto save(GymInfoDto dto);
    List<String> getAllAddresses();
    Optional<GymInfoDto> getByAddress(String gymAddress);
}
