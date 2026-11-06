package com.sams.controller;

import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sams.model.TrainingLoad;
import com.sams.service.*;

@Controller
@RequestMapping("/loads")
public class LoadController {
    private final InjuryService injuryService;
    private final PlayerService playerService;

    public LoadController(InjuryService i, PlayerService p) {
        this.injuryService = i;
        this.playerService = p;
    }

    @GetMapping
    public String page(Model model) {
        TrainingLoad t = new TrainingLoad();
        t.setDate(LocalDate.now());
        model.addAttribute("load", t);
        model.addAttribute("players", playerService.getAll());
        model.addAttribute("loads", injuryService.recent());
        return "loads";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("load") TrainingLoad t) {
        injuryService.save(t);
        return "redirect:/loads";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        injuryService.delete(id);
        return "redirect:/loads";
    }

    @GetMapping("/sample")
    public String sample() {
        injuryService.generateSample();
        return "redirect:/injury";
    }
}