package com.sams.controller;

import java.util.*;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sams.service.*;

@Controller
@RequestMapping("/injury")
public class InjuryController {
    private final InjuryService injuryService;
    private final PlayerService playerService;

    public InjuryController(InjuryService i, PlayerService p) {
        this.injuryService = i;
        this.playerService = p;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("risks", injuryService.allRisks());
        return "injury";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        List<String> labels = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> e : injuryService.dailyLoads(id).entrySet()) {
            labels.add(e.getKey().toString());
            values.add(e.getValue());
        }
        model.addAttribute("risk", injuryService.calculate(playerService.getById(id)));
        model.addAttribute("labels", labels);
        model.addAttribute("values", values);
        return "injury-detail";
    }
}