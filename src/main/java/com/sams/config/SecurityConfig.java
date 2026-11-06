package com.sams.config;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/login", "/register", "/forgot", "/forgot/**", "/css/**", "/js/**").permitAll()
            		.requestMatchers("/scout/**").hasAnyRole("ADMIN", "SCOUT")
                .requestMatchers("/loads/**", "/injury/**", "/players/**", "/teams/**")
                    .hasAnyRole("ADMIN", "COACH")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/login?logout"));
        return http.build();
    }
}