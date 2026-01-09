package com.brand.prelaunch.controller;

import com.brand.prelaunch.model.Product;
import com.brand.prelaunch.model.Subscriber;
import com.brand.prelaunch.service.EmailService;
import com.brand.prelaunch.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WaitlistController {

    private final EmailService emailService;
    private final ProductService productService;

    public WaitlistController(EmailService emailService, ProductService productService) {
        this.emailService = emailService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        // 1. Luăm toate produsele din baza de date
        List<Product> allProducts = productService.getAllProducts();

        // 2. Filtrăm FELPE (HANORACE)
        // Căutăm produsele care au "FELPA" sau "HOODIE" în nume
        List<Product> hoodies = allProducts.stream()
                .filter(p -> p.getName().toUpperCase().contains("FELPA") || p.getName().toUpperCase().contains("HOODIE"))
                .collect(Collectors.toList());

        // 3. Filtrăm T-SHIRTS (TRICOURI)
        List<Product> tshirts = allProducts.stream()
                .filter(p -> p.getName().toUpperCase().contains("T-SHIRT") || p.getName().toUpperCase().contains("TEE"))
                .collect(Collectors.toList());

        // 4. Filtrăm PANTALONI
        List<Product> pants = allProducts.stream()
                .filter(p -> p.getName().toUpperCase().contains("PANTALONI") || p.getName().toUpperCase().contains("PANT"))
                .collect(Collectors.toList());

        // 5. Le trimitem separat către HTML
        model.addAttribute("hoodies", hoodies);
        model.addAttribute("tshirts", tshirts);
        model.addAttribute("pants", pants);

        // Date standard
        model.addAttribute("subscriber", new Subscriber());
        model.addAttribute("serverTime", System.currentTimeMillis());

        return "index";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable String id, Model model) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("serverTime", System.currentTimeMillis());
                    return "product_detail";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/join")
    public String joinWaitlist(@Valid @ModelAttribute Subscriber subscriber,
                               BindingResult bindingResult,
                               Model model) {
        if (subscriber.getWebsiteUrl() != null && !subscriber.getWebsiteUrl().isEmpty()) {
            return "success";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("serverTime", System.currentTimeMillis());
            // Dacă e eroare, trebuie să re-populăm listele, altfel pagina crapă
            List<Product> allProducts = productService.getAllProducts();
            model.addAttribute("hoodies", allProducts.stream().filter(p -> p.getName().contains("FELPA")).collect(Collectors.toList()));
            model.addAttribute("tshirts", allProducts.stream().filter(p -> p.getName().contains("T-SHIRT")).collect(Collectors.toList()));
            model.addAttribute("pants", allProducts.stream().filter(p -> p.getName().contains("PANTALONI")).collect(Collectors.toList()));
            return "index";
        }

        emailService.save(subscriber.getEmail());
        return "success";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }
}