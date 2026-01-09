package com.brand.prelaunch.controller;

import com.brand.prelaunch.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public String viewCart(Model model) {
        // --- AICI ESTE REZOLVAREA PENTRU AFIȘARE ---

        // IMPORTANT: Folosim getCartDetails()!
        // Asta transformă ID-ul "1" în Produsul complet (Nume, Preț, Imagine)
        // Dacă foloseai getItems(), HTML-ul primea doar textul "1" și nu știa ce să afișeze.
        model.addAttribute("cartItems", cartService.getCartDetails());

        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("sessionId", "TERM-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase());

        return "cart";
    }

    // --- AICI ESTE REZOLVAREA PENTRU LINK-UL DIN HTML ---

    // Folosim @PathVariable ca să accepte formatul: /cart/add/1
    @PostMapping("/add/{productId}")
    public String addToCart(@PathVariable("productId") String productId) {
        cartService.addToCart(productId);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("productId") String productId) {
        cartService.remove(productId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam("email") String email, Model model) {
        try {
            cartService.checkout(email);
            return "redirect:/success";
        } catch (Exception e) {
            return "redirect:/cart?error=" + e.getMessage();
        }
    }
}