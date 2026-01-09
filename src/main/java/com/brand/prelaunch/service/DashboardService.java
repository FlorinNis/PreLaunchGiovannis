package com.brand.prelaunch.service;

import com.brand.prelaunch.model.Order;
import com.brand.prelaunch.model.Product;
import com.brand.prelaunch.repository.OrderRepository;
import com.brand.prelaunch.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public DashboardService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Double getTotalRevenue() {
        // Assuming all orders in DB are valid/PAID for now since we have no payment gateway integration yet.
        // In a real scenario, filter by status == PAID.
        return orderRepository.findAll().stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }

    public long getTotalOrders() {
        return orderRepository.count();
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getStock() < 10)
                .collect(Collectors.toList());
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Order> getRecentOrders() {
        // Fetch last 10 orders, sorted by ID descending (proxy for date if IDs are sequential)
        // or strictly by date if Sort supports it. Using ID desc for simplicity here.
        return orderRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
    }

    public void updateStock(String productId, int newStock) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setStock(newStock);
            productRepository.save(product);
        });
    }
}