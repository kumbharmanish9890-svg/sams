package com.sams.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sams.model.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {}