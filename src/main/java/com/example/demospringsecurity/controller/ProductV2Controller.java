package com.example.demospringsecurity.controller;

import com.example.demospringsecurity.entity.Product;
import com.example.demospringsecurity.entity.Role;
import com.example.demospringsecurity.entity.User;
import com.example.demospringsecurity.service.ProductService;
import com.example.demospringsecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

import static com.example.demospringsecurity.controller.ProductV2Controller.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class ProductV2Controller {
    public static final String REQUEST_MAPPING = "/api/v2/product";

    private final ProductService productService;
    private final UserService userService;

    //for USER and ADMIN
    @GetMapping
    public List<Product> findAll(HttpServletRequest request) {
        User current = findCurrent(request);
        if (current == null || !List.of(Role.ADMIN, Role.USER).contains(current.getRole())) throw new AccessDeniedException("You don't have access to the resource");

        return productService.findAll();
    }

    //for ADMIN
    @PostMapping
    public Product create(@RequestBody Product product, HttpServletRequest request) {
        User current = findCurrent(request);
        if (current == null || current.getRole() != Role.ADMIN) throw new AccessDeniedException("You don't have access to the resource");


        return productService.save(product);
    }


    private User findCurrent(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null) return null;

        String[] authTokens = authHeader.split(" ");
        if (authTokens.length != 2 || !authTokens[0].equalsIgnoreCase("Basic")) return null;

        String[] userCredentials = new String(Base64.getDecoder().decode(authTokens[1])).split(":");
        if (userCredentials.length != 2) return null;

        String email = userCredentials[0];
        String password = userCredentials[1];

        User user = userService.findByEmail(email);
        if (user == null || !password.equals(user.getPassword())) return null;

        return user;
    }
}
