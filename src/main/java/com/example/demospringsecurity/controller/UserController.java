package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.entity.User;
import com.example.demospringsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demospringsecurity.controller.UserController.REQUEST_MAPPING;

@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserController {
    public static final String REQUEST_MAPPING = "/api/v1/user";

    private final UserService userService;

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{email}")
    public User findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

}
