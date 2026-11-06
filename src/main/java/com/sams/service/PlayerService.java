package com.sams.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sams.model.Player;
import com.sams.repository.PlayerRepository;
import com.sams.repository.ScoutProfileRepository;
import com.sams.repository.TrainingLoadRepository;

@Service
public class PlayerService {
    private final PlayerRepository repo;
    private final TrainingLoadRepository loadRepo;
    private final ScoutProfileRepository scoutRepo;

    public PlayerService(PlayerRepository repo, TrainingLoadRepository loadRepo,
                         ScoutProfileRepository scoutRepo) {
        this.repo = repo;
        this.loadRepo = loadRepo;
        this.scoutRepo = scoutRepo;
    }

    public List<Player> getAll() { return repo.findAll(); }
    public Player save(Player p) { return repo.save(p); }
    public Player getById(Long id) { return repo.findById(id).orElseThrow(); }
    public long count() { return repo.count(); }

    @Transactional
    public void delete(Long id) {
        Player p = repo.findById(id).orElse(null);

        loadRepo.deleteByPlayerId(id);   
        scoutRepo.deleteByPlayerId(id);  
        repo.deleteById(id);             

        if (p != null && p.getPhoto() != null && !p.getPhoto().isBlank()) {
            try {
                String fileName = Paths.get(p.getPhoto()).getFileName().toString();
                Files.deleteIfExists(Paths.get("uploads", "players").toAbsolutePath().resolve(fileName));
            } catch (IOException e) {
                
            }
        }
    }
}