package com.brand.prelaunch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String id; // Manual ID: "001", "002"

    private String name;
    private String description; // Using this for specs/details
    private Integer price;      // Stored as Integer (e.g., 120)
    private String pattern;     // CSS class suffix: "scanline", "noise"
    private int stock;
    private String badge;       // "ANCHOR_ITEM", "LIMITED"

    // Default constructor for JPA
    public Product() {
    }

    public Product(String id, String name, Integer price, String badge, String description, String pattern, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.badge = badge;
        this.description = description;
        this.pattern = pattern;
        this.stock = stock;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public String getPattern() { return pattern; }
    public void setPattern(String pattern) { this.pattern = pattern; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

}