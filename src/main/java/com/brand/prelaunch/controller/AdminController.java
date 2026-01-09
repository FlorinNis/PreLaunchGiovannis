package com.brand.prelaunch.controller;

import com.brand.prelaunch.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.brand.prelaunch.service.GlobalStateService; // Import


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final DashboardService dashboardService;
    private final GlobalStateService globalStateService; // Inject

    public AdminController(DashboardService dashboardService, GlobalStateService globalStateService) {
        this.dashboardService = dashboardService;
        this.globalStateService = globalStateService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ... existing attributes ...
        model.addAttribute("totalRevenue", dashboardService.getTotalRevenue());
        model.addAttribute("totalOrders", dashboardService.getTotalOrders());
        model.addAttribute("lowStockProducts", dashboardService.getLowStockProducts());
        model.addAttribute("recentOrders", dashboardService.getRecentOrders());
        model.addAttribute("allProducts", dashboardService.getAllProducts());

        // Add Drop Status
        model.addAttribute("isDropLive", globalStateService.isDropLive());

        return "admin/dashboard";
    }

    // Convenience redirect for /admin to /admin/dashboard
    @GetMapping("")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/stock/update")
    public String updateStock(@RequestParam String productId, @RequestParam int newStock) {
        dashboardService.updateStock(productId, newStock);
        return "redirect:/admin/dashboard";
    }

    // --- DROP CONTROL ---
    @PostMapping("/drop/toggle")
    public String toggleDrop(@RequestParam boolean active) {
        globalStateService.setDropLive(active);
        return "redirect:/admin/dashboard";
    }
}