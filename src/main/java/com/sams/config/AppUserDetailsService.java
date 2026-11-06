package com.sams.config;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import com.sams.model.AppUser;
import com.sams.repository.AppUserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository repo;

    public AppUserDetailsService(AppUserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.withUsername(u.getUsername())
                   .password(u.getPassword())
                   .roles(u.getRole())
                   .build();
    }
}