package com.brand.prelaunch.repository;

import com.brand.prelaunch.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    // Custom query methods can be defined here (e.g., findByName)
}