package com.example.demospringsecurity.repository;

import com.example.demospringsecurity.entity.Product;
import com.example.demospringsecurity.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository extends BaseRepository<Product> {
    public ProductRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, Product.class);
    }

    public List<Product> findAll() {
        return mongoTemplate.findAll(Product.class);
    }
}
