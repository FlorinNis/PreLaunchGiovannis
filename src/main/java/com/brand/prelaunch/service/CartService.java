package com.brand.prelaunch.service;

import com.brand.prelaunch.model.Order;
import com.brand.prelaunch.model.OrderItem;
import com.brand.prelaunch.model.Product;
import com.brand.prelaunch.repository.OrderRepository;
import com.brand.prelaunch.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Random;

@Service
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartService {

    private final Map<String, Integer> cartItems = new HashMap<>(); // Stores Product ID -> Quantity
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public CartService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public void addToCart(String productId) {
        cartItems.put(productId, cartItems.getOrDefault(productId, 0) + 1);
    }

    public void remove(String productId) {
        cartItems.remove(productId);
    }

    public Map<String, Integer> getItems() {
        return cartItems;
    }

    public Double getTotal() {
        return cartItems.entrySet().stream()
                .mapToDouble(entry -> {
                    Product p = productRepository.findById(entry.getKey()).orElse(null);
                    if (p != null) {
                        return p.getPrice() * entry.getValue();
                    }
                    return 0.0;
                })
                .sum();
    }

    // Helper to get full product objects for the view
    public Map<Product, Integer> getCartDetails() {
        Map<Product, Integer> details = new HashMap<>();
        cartItems.forEach((id, qty) -> {
            productRepository.findById(id).ifPresent(p -> details.put(p, qty));
        });
        return details;
    }

    @Transactional
    public Order checkout(String email) { // Changed return type to Order to pass it to the view
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("CART_EMPTY");
        }

        Order order = new Order();
        order.setCustomerEmail(email);
        order.setTotalAmount(getTotal());
        
        // --- GAMIFICATION LOGIC ---
        // 1. Generate AGENT_ID (Simple hash-like string)
        String rawString = email + System.currentTimeMillis();
        String agentId = "AGT-" + UUID.nameUUIDFromBytes(rawString.getBytes()).toString().substring(0, 8).toUpperCase();
        order.setAgentId(agentId);

        // 2. Assign DROP_RANK (Current Count + Random Buffer)
        long currentCount = orderRepository.count();
        long randomBuffer = new Random().nextInt(50) + 50; // Random between 50-100
        order.setDropRank(currentCount + randomBuffer);
        // -------------------------

        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
             // ... existing loop code ...
            String productId = entry.getKey();
            Integer quantity = entry.getValue();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("INVALID_PRODUCT: " + productId));

            if (product.getStock() < quantity) {
                throw new IllegalStateException("OUT_OF_STOCK: " + product.getName());
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            OrderItem item = new OrderItem(product.getName(), quantity, product.getPrice());
            order.addItem(item);
        }

        Order savedOrder = orderRepository.save(order); // Save and capture
        cartItems.clear();
        return savedOrder; // Return the saved order
    }
}