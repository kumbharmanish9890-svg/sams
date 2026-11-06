package com.sams.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sams.model.*;
import com.sams.service.*;

@Controller
@RequestMapping("/scout")
public class ScoutController {
    private final ScoutService scoutService;
    private final PlayerService playerService;

    public ScoutController(ScoutService s, PlayerService p) {
        this.scoutService = s;
        this.playerService = p;
    }

    @GetMapping
    public String page(Model model) {
        model.addAttribute("profile", new ScoutProfile());
        model.addAttribute("players", playerService.getAll());
        model.addAttribute("profiles", scoutService.getAll());
        return "scout";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("profile") ScoutProfile s) {
        scoutService.save(s);
        return "redirect:/scout";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        scoutService.delete(id);
        return "redirect:/scout";
    }

    @GetMapping("/sample")
    public String sample() {
        scoutService.generateSample();
        return "redirect:/scout";
    }

    @GetMapping("/compare")
    public String compare(@RequestParam Long a, @RequestParam Long b, Model model) {
        ScoutProfile pa = scoutService.byPlayer(a);
        ScoutProfile pb = scoutService.byPlayer(b);
        if (pa == null || pb == null) 
        	return "redirect:/scout?missing";
        model.addAttribute("nameA", pa.getPlayer().getName());
        model.addAttribute("nameB", pb.getPlayer().getName());
        model.addAttribute("valuesA", List.of(pa.getPace(), pa.getShooting(), pa.getPassing(),
                pa.getDribbling(), pa.getDefending(), pa.getPhysical()));
        model.addAttribute("valuesB", List.of(pb.getPace(), pb.getShooting(), pb.getPassing(),
                pb.getDribbling(), pb.getDefending(), pb.getPhysical()));
        return "scout-compare";
    }

    @GetMapping("/similar")
    public String similar(@RequestParam Long playerId, @RequestParam double budget, Model model) {
        ScoutProfile target = scoutService.byPlayer(playerId);
        if (target == null) return "redirect:/scout";

        model.addAttribute("target", target);
        model.addAttribute("budget", budget);
        model.addAttribute("results", scoutService.similar(playerId, budget));
        return "scout-similar";
    }
}