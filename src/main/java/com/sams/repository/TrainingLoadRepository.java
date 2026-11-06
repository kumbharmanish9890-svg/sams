package com.sams.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sams.model.TrainingLoad;

public interface TrainingLoadRepository extends JpaRepository<TrainingLoad, Long> {
    List<TrainingLoad> findByPlayerIdAndDateBetween(Long playerId, LocalDate from, LocalDate to);
    List<TrainingLoad> findTop20ByOrderByDateDescIdDesc();
    void deleteByPlayerId(Long playerId);
}