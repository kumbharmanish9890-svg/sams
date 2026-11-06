package com.sams.service;

import java.util.*;
import org.springframework.stereotype.Service;
import com.sams.dto.SimilarResult;
import com.sams.model.*;
import com.sams.repository.*;

@Service
public class ScoutService {
    private final ScoutProfileRepository repo;
    private final PlayerRepository playerRepo;

    public ScoutService(ScoutProfileRepository repo, PlayerRepository playerRepo) {
        this.repo = repo;
        this.playerRepo = playerRepo;
    }

    public List<ScoutProfile> getAll() { return repo.findAll(); }

    public ScoutProfile byPlayer(Long playerId) {
        List<ScoutProfile> list = repo.findByPlayerId(playerId);
        return list.isEmpty() ? null : list.get(0);
    }

    public ScoutProfile save(ScoutProfile s) {
        if (s.getPlayer() != null) {
            ScoutProfile old = byPlayer(s.getPlayer().getId());
            if (old != null) s.setId(old.getId());
        }
        return repo.save(s);
    }

    public void delete(Long id) { repo.deleteById(id); }

    public void generateSample() {
        Random r = new Random();
        for (Player p : playerRepo.findAll()) {
            if (byPlayer(p.getId()) != null) continue;
            ScoutProfile s = new ScoutProfile();
            s.setPlayer(p);
            s.setPace(40 + r.nextInt(56));
            s.setShooting(40 + r.nextInt(56));
            s.setPassing(40 + r.nextInt(56));
            s.setDribbling(40 + r.nextInt(56));
            s.setDefending(40 + r.nextInt(56));
            s.setPhysical(40 + r.nextInt(56));
            s.setMarketValue(Math.round((0.5 + r.nextDouble() * 14.5) * 10) / 10.0);
            repo.save(s);
        }
    }

    private double distance(ScoutProfile a, ScoutProfile b) {
        double d = Math.pow(a.getPace() - b.getPace(), 2)
                 + Math.pow(a.getShooting() - b.getShooting(), 2)
                 + Math.pow(a.getPassing() - b.getPassing(), 2)
                 + Math.pow(a.getDribbling() - b.getDribbling(), 2)
                 + Math.pow(a.getDefending() - b.getDefending(), 2)
                 + Math.pow(a.getPhysical() - b.getPhysical(), 2);
        return Math.sqrt(d);
    }

    public List<SimilarResult> similar(Long playerId, double budget) {
        List<SimilarResult> results = new ArrayList<>();
        ScoutProfile target = byPlayer(playerId);
        if (target == null) return results;

        double maxDistance = Math.sqrt(6 * 100.0 * 100.0); 
        for (ScoutProfile s : repo.findAll()) {
            if (s.getPlayer() == null || s.getPlayer().getId().equals(playerId)) continue;
            if (s.getMarketValue() > budget) continue;
            double sim = 100.0 - (distance(target, s) / maxDistance * 100.0);
            sim = Math.round(sim * 10) / 10.0;
            results.add(new SimilarResult(s.getPlayer().getId(), s.getPlayer().getName(),
                    s.getPlayer().getPosition(), s.getMarketValue(), sim));
        }
        results.sort((x, y) -> Double.compare(y.getSimilarity(), x.getSimilarity()));
        return results.size() > 5 ? results.subList(0, 5) : results;
    }
}