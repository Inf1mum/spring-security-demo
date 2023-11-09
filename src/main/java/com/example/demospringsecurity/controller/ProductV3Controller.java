package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.entity.Product;
import com.example.demospringsecurity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demospringsecurity.controller.ProductV3Controller.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class ProductV3Controller {
    public static final String REQUEST_MAPPING = "/api/v3/product";

    private final ProductService productService;

    @GetMapping
    public List<Product> findAll() {
        return productService.findAll();
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }
}
