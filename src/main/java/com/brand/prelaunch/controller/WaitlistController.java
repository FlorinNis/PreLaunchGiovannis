package com.brand.prelaunch.controller;

import com.brand.prelaunch.model.Subscriber;
import com.brand.prelaunch.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class WaitlistController {

    private final EmailService emailService;

    public WaitlistController(EmailService emailService) {
        this.emailService = emailService;
    }

    // Simple Record to hold Product Data (Java 17 feature)
    public record Product(String name, String price, String badge, String description) {}

    @GetMapping("/")
    public String index(Model model) {
        // 1. Initialize Form
        model.addAttribute("subscriber", new Subscriber());

        // 2. Server Time for Countdown [cite: 151]
        model.addAttribute("serverTime", System.currentTimeMillis());

        // 3. THE DROP LIST (Source 2: Pricing Psychology) [cite: 330]
        List<Product> dropItems = List.of(
                new Product("HOODIE_V1_HEAVY", "$120", "ANCHOR_ITEM", "400GSM // PRESTIGE_WEIGHT"),
                new Product("GRAPHIC_TEE_04", "$48", "FAST_MOVER", "SYSTEM_1_IMPULSE // VINTAGE_WASH")
        );
        model.addAttribute("products", dropItems);

        return "index";
    }

    @PostMapping("/join")
    public String joinWaitlist(@Valid @ModelAttribute Subscriber subscriber,
                               BindingResult bindingResult,
                               Model model) {
        if (subscriber.getWebsiteUrl() != null && !subscriber.getWebsiteUrl().isEmpty()) {
            return "success"; // Silent Drop for Bots [cite: 179]
        }
        if (bindingResult.hasErrors()) {
            // Re-populate data if validation fails so page doesn't look broken
            model.addAttribute("serverTime", System.currentTimeMillis());
            model.addAttribute("products", List.of(
                    new Product("HOODIE_V1_HEAVY", "$120", "ANCHOR_ITEM", "400GSM // PRESTIGE_WEIGHT"),
                    new Product("GRAPHIC_TEE_04", "$48", "FAST_MOVER", "SYSTEM_1_IMPULSE // VINTAGE_WASH")
            ));
            return "index";
        }
        emailService.save(subscriber.getEmail());
        return "success";
    }
}