package com.brand.prelaunch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private int quantity;
    private Integer priceAtTimeOfPurchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem() {}

    public OrderItem(String productName, int quantity, Integer priceAtTimeOfPurchase) {
        this.productName = productName;
        this.quantity = quantity;
        this.priceAtTimeOfPurchase = priceAtTimeOfPurchase;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Integer getPriceAtTimeOfPurchase() { return priceAtTimeOfPurchase; }
    public void setPriceAtTimeOfPurchase(Integer priceAtTimeOfPurchase) { this.priceAtTimeOfPurchase = priceAtTimeOfPurchase; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
}