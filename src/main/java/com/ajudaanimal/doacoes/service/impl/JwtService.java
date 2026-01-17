package com.ajudaanimal.doacoes.service.impl;

import org.springframework.stereotype.Service;

@Service
public class JwtService {

    // Accept tokens of form "demo-<userId>" or plain userId for demo
    public String getUserIdFromToken(String token){
        if (token == null) return null;
        token = token.trim();
        if (token.startsWith("Bearer ")) token = token.substring(7).trim();
        if (token.startsWith("demo-")) return token.substring(5);
        // if token looks like a user id
        if (token.matches("^[a-zA-Z0-9_-]{1,40}$")) return token;
        return null; // unknown format
    }
}
