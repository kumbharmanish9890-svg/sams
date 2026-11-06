package com.sams.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.sams.model.AppUser;
import com.sams.repository.AppUserRepository;

@Component
public class DataSeeder implements CommandLineRunner {
    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public DataSeeder(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repo.count() > 0) return;
        create("admin", System.getenv().getOrDefault("ADMIN_PASSWORD", "admin123"), "ADMIN");
        create("coach", System.getenv().getOrDefault("COACH_PASSWORD", "coach123"), "COACH");
        create("scout", System.getenv().getOrDefault("SCOUT_PASSWORD", "scout123"), "SCOUT");
    }

    private void create(String username, String password, String role) {
        AppUser u = new AppUser();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        repo.save(u);
    }
}