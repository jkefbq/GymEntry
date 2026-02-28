package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.GymInfo;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GymInfoRepository extends JpaRepository<@NonNull GymInfo, @NonNull UUID> {
    @Query("SELECT g.address FROM GymInfo g")
    List<String> getAllAddresses();
    Optional<GymInfo> getByAddress(String address);
}
