package com.sams.controller;

import java.util.List;
import java.util.regex.Pattern;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.sams.model.AppUser;
import com.sams.repository.AppUserRepository;

@Controller
public class AuthController {

    private static final List<String> QUESTIONS = List.of(
        "What was the name of your first school?",
        "What is your favourite sport?",
        "What is the name of your childhood best friend?",
        "In which city were you born?");
    private static final List<String> ROLES = List.of("COACH", "SCOUT");
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_.]{3,30}$");

    private final AppUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(AppUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    private String normalize(String s) {
        return s.trim().toLowerCase();
    }

    // ---------- Registration ----------
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("questions", QUESTIONS);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String confirm, @RequestParam String role,
                           @RequestParam String question, @RequestParam String answer,
                           Model model) {
        username = username.trim();
        String error = null;
        if (!USERNAME.matcher(username).matches()) {
            error = "Username must be 3-30 characters: letters, numbers, dot or underscore.";
        } else if (password.length() < 8) {
            error = "Password must be at least 8 characters.";
        } else if (!password.equals(confirm)) {
            error = "Passwords do not match.";
        } else if (!ROLES.contains(role)) {
            error = "Invalid role.";
        } else if (!QUESTIONS.contains(question)) {
            error = "Invalid security question.";
        } else if (answer.isBlank()) {
            error = "Security answer is required.";
        } else if (repo.findByUsername(username).isPresent()) {
            error = "This username is already taken.";
        }

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("questions", QUESTIONS);
            model.addAttribute("username", username);
            return "register";
        }

        AppUser u = new AppUser();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRole(role);
        u.setSecurityQuestion(question);
        u.setSecurityAnswer(encoder.encode(normalize(answer)));
        repo.save(u);
        return "redirect:/login?registered";
    }

    // ---------- Forgot password ----------
    @GetMapping("/forgot")
    public String forgot() {
        return "forgot";
    }

    // Step 1: username -> show the security question
    @PostMapping("/forgot")
    public String forgotStep1(@RequestParam String username, Model model) {
        AppUser u = repo.findByUsername(username.trim()).orElse(null);
        if (u == null || u.getSecurityQuestion() == null) {
            model.addAttribute("error", "No account with a security question was found for this username.");
            return "forgot";
        }
        model.addAttribute("username", u.getUsername());
        model.addAttribute("question", u.getSecurityQuestion());
        return "forgot";
    }

    // Step 2: answer + new password
    @PostMapping("/forgot/reset")
    public String reset(@RequestParam String username, @RequestParam String answer,
                        @RequestParam String newPassword, @RequestParam String confirm,
                        Model model) {
        AppUser u = repo.findByUsername(username).orElse(null);
        if (u == null || u.getSecurityAnswer() == null) {
            return "redirect:/forgot";
        }

        String error = null;
        if (newPassword.length() < 8) {
            error = "Password must be at least 8 characters.";
        } else if (!newPassword.equals(confirm)) {
            error = "Passwords do not match.";
        } else if (!encoder.matches(normalize(answer), u.getSecurityAnswer())) {
            error = "Security answer is incorrect.";
        }

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("username", u.getUsername());
            model.addAttribute("question", u.getSecurityQuestion());
            return "forgot";
        }

        u.setPassword(encoder.encode(newPassword));
        repo.save(u);
        return "redirect:/login?reset";
    }
}