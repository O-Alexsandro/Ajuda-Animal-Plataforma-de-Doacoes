package com.ajudaanimal.doacoes.controller.chat;

import com.ajudaanimal.doacoes.service.impl.ConversaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ConversaService conversationService;

    @GetMapping("/conversations")
    public ResponseEntity<?> listConvos(@RequestHeader(value = "Authorization", required = false) String auth){
        String token = (auth!=null && auth.startsWith("Bearer ")) ? auth.substring(7) : auth;
        String userId = token;
        if (userId == null) return ResponseEntity.badRequest().body(Map.of("error","Missing Authorization token"));
        return ResponseEntity.ok(conversationService.listConversasForUser(userId));
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<?> history(@PathVariable String id){
        return ResponseEntity.ok(conversationService.getMessages(id));
    }
}