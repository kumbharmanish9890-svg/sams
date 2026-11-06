package com.sams.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sams.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}