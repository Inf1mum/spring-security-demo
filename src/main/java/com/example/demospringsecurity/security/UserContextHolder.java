package com.example.demospringsecurity.security;

import com.example.demospringsecurity.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class UserContextHolder {

    public static AuthenticationToken getAuthentication() {
        return (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static User getUser() {
        return getAuthentication().getUser();
    }

    public static String getAuthHeader() {
        AuthenticationToken authentication = (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthHeader();
    }

    public static String getEmail(){
        return getUser().getEmail();
    }
}
