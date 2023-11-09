package com.example.demospringsecurity.repository;

import com.example.demospringsecurity.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends BaseRepository<User> {
    public UserRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate, User.class);
    }

    public User findByEmail(String email) {
        Query query = Query.query(Criteria.where("email").is(email));
        return mongoTemplate.findOne(query, User.class);
    }

    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }
}
