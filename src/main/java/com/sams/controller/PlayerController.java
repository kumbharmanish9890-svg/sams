package com.sams.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.sams.model.Player;
import com.sams.service.*;

@Controller
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;
    private final TeamService teamService;

    public PlayerController(PlayerService p, TeamService t) {
        this.playerService = p;
        this.teamService = t;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("players", playerService.getAll());
        return "players";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("teams", teamService.getAll());
        return "player-form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("player", playerService.getById(id));
        model.addAttribute("teams", teamService.getAll());
        return "player-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Player player,
                       @RequestParam("photoFile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String type = file.getContentType() == null ? "" : file.getContentType();
            String ext;
            switch (type) {
                case "image/jpeg": ext = ".jpg"; break;
                case "image/png":  ext = ".png"; break;
                case "image/webp": ext = ".webp"; break;
                default: return "redirect:/players?badimage";
            }
            Path dir = Paths.get("uploads", "players").toAbsolutePath();
            Files.createDirectories(dir);
            String name = UUID.randomUUID() + ext;
            file.transferTo(dir.resolve(name));
            player.setPhoto(name);
        }
        playerService.save(player);
        return "redirect:/players";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        playerService.delete(id);
        return "redirect:/players";
    }
}