package com.sams.config;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addUserInfo(Model model, Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken) return;

        boolean admin = hasRole(auth, "ROLE_ADMIN");
        boolean coach = hasRole(auth, "ROLE_COACH");
        boolean scout = hasRole(auth, "ROLE_SCOUT");

        model.addAttribute("currentUser", auth.getName());
        model.addAttribute("canCoach", admin || coach);
        model.addAttribute("canScout", admin || scout);
    }

    private boolean hasRole(Authentication auth, String role) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }
}