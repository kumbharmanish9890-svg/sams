package com.sams.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.sams.model.Player;
import com.sams.repository.PlayerRepository;

@Component
public class PlayerConverter implements Converter<String, Player> {
    private final PlayerRepository repo;

    public PlayerConverter(PlayerRepository repo) { this.repo = repo; }

    @Override
    public Player convert(String source) {
        if (source == null || source.isBlank()) return null;
        return repo.findById(Long.valueOf(source)).orElse(null);
    }
}