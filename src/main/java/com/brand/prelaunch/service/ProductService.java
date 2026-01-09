package com.brand.prelaunch.service;

import com.brand.prelaunch.model.Product; // <--- IMPORT-UL CORECT E ESENȚIAL
import com.brand.prelaunch.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    // ȘTERGE ORICE LINIE CARE ÎNCEPE CU: public record Product(...)

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Acum returnează tipul corect din model, luând datele din baza de date
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }
}