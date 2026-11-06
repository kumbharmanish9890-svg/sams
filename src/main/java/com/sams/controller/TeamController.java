package com.sams.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sams.model.Team;
import com.sams.service.TeamService;

@Controller
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService t) { this.teamService = t; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("teams", teamService.getAll());
        model.addAttribute("team", new Team());
        return "teams";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Team team) {
        teamService.save(team);
        return "redirect:/teams";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        try {
            teamService.delete(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return "redirect:/teams?inuse";
        }
        return "redirect:/teams";
    }
}