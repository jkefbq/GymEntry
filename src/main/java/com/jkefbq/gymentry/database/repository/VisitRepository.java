package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.Visit;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<@NonNull Visit, @NonNull UUID> {
    @Query("SELECT v FROM Visit v WHERE v.gym.address = :address AND v.createdAt >= :from AND v.createdAt <= :to")
    List<Visit> getAllForPeriod(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("address") String address);
}
