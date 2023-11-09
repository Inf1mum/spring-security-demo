package com.example.demospringsecurity.service;

import com.example.demospringsecurity.entity.Product;
import com.example.demospringsecurity.entity.User;
import com.example.demospringsecurity.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product update(Product product) {
        return productRepository.update(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

}
