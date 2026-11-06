package com.sams.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sams.model.ScoutProfile;

public interface ScoutProfileRepository extends JpaRepository<ScoutProfile, Long> {
    List<ScoutProfile> findByPlayerId(Long playerId);
    void deleteByPlayerId(Long playerId);
}