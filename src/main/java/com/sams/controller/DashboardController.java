package com.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sams.service.*;

@Controller
public class DashboardController {
    private final PlayerService playerService;
    private final TeamService teamService;
    private final InjuryService injuryService;

    public DashboardController(PlayerService p, TeamService t, InjuryService i) {
        this.playerService = p;
        this.teamService = t;
        this.injuryService = i;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("playerCount", playerService.count());
        model.addAttribute("teamCount", teamService.count());
        model.addAttribute("alertCount", injuryService.highRiskCount());
        model.addAttribute("risks", injuryService.allRisks());
        return "dashboard";
    }
}