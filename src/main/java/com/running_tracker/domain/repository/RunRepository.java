package com.running_tracker.domain.repository;

import com.running_tracker.domain.entity.Run;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface RunRepository extends JpaRepository<Run, UUID> {

    List<Run> findAllByUserIdAndStartDatetimeBetween(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime);

    List<Run> findAllByUserId(UUID userId);
}
