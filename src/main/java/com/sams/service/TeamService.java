package com.sams.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.sams.model.Team;
import com.sams.repository.TeamRepository;

@Service
public class TeamService {
    private final TeamRepository repo;

    public TeamService(TeamRepository repo) {
        this.repo = repo;
    }

    public List<Team> getAll() { return repo.findAll(); }
    public Team save(Team t) { return repo.save(t); }
    public Team getById(Long id) { return repo.findById(id).orElseThrow(); }
    public void delete(Long id) { repo.deleteById(id); }
    public long count() { return repo.count(); }
}