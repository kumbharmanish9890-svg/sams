package com.sams.service;

import java.time.LocalDate;
import java.util.*;
import org.springframework.stereotype.Service;
import com.sams.dto.PlayerRisk;
import com.sams.model.*;
import com.sams.repository.*;

@Service
public class InjuryService {
    private final TrainingLoadRepository loadRepo;
    private final PlayerRepository playerRepo;

    public InjuryService(TrainingLoadRepository loadRepo, PlayerRepository playerRepo) {
        this.loadRepo = loadRepo;
        this.playerRepo = playerRepo;
    }

    public PlayerRisk calculate(Player p) {
        LocalDate today = LocalDate.now();
        List<TrainingLoad> last28 =
            loadRepo.findByPlayerIdAndDateBetween(p.getId(), today.minusDays(27), today);

        double acuteSum = 0, chronicSum = 0;
        for (TrainingLoad t : last28) {
            chronicSum += t.getLoad();
            if (!t.getDate().isBefore(today.minusDays(6))) acuteSum += t.getLoad();
        }
        double acute = acuteSum / 7.0;
        double chronic = chronicSum / 28.0;
        double acwr = chronic > 0 ? acute / chronic : 0;
        acwr = Math.round(acwr * 100.0) / 100.0;

        String status, badge;
        if (chronic == 0)      { status = "No Data";    badge = "secondary"; }
        else if (acwr < 0.8)   { status = "Low Load";   badge = "info"; }
        else if (acwr <= 1.3)  { status = "Safe";       badge = "success"; }
        else if (acwr <= 1.5)  { status = "Warning";    badge = "warning"; }
        else                   { status = "High Risk";  badge = "danger"; }

        return new PlayerRisk(p.getId(), p.getName(),
            Math.round(acute * 10) / 10.0, Math.round(chronic * 10) / 10.0,
            acwr, status, badge);
    }

    public List<PlayerRisk> allRisks() {
        List<PlayerRisk> list = new ArrayList<>();
        for (Player p : playerRepo.findAll()) list.add(calculate(p));
        return list;
    }

    public long highRiskCount() {
        return allRisks().stream().filter(r -> r.getStatus().equals("High Risk")).count();
    }

   
    public Map<LocalDate, Integer> dailyLoads(Long playerId) {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Integer> map = new LinkedHashMap<>();
        for (int i = 27; i >= 0; i--) map.put(today.minusDays(i), 0);
        for (TrainingLoad t : loadRepo.findByPlayerIdAndDateBetween(playerId, today.minusDays(27), today)) {
            map.merge(t.getDate(), t.getLoad(), Integer::sum);
        }
        return map;
    }

    public void generateSample() {
        Random r = new Random();
        LocalDate today = LocalDate.now();
        for (Player p : playerRepo.findAll()) {
            boolean spike = r.nextInt(100) < 35; 
            for (int i = 27; i >= 0; i--) {
                TrainingLoad t = new TrainingLoad();
                t.setPlayer(p);
                t.setDate(today.minusDays(i));
                int rpe = 4 + r.nextInt(4);
                int dur = 50 + r.nextInt(40);
                if (spike && i < 7) { rpe = Math.min(10, rpe + 3); dur += 40; }
                t.setRpe(rpe);
                t.setDurationMin(dur);
                loadRepo.save(t);
            }
        }
    }

    public List<TrainingLoad> recent() { return loadRepo.findTop20ByOrderByDateDescIdDesc(); }
    public TrainingLoad save(TrainingLoad t) { return loadRepo.save(t); }
    public void delete(Long id) { loadRepo.deleteById(id); }
}