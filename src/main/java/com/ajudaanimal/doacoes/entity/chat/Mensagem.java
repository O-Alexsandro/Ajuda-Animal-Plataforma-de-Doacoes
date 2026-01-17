package com.ajudaanimal.doacoes.entity.chat;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

@Entity
public class Mensagem {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "conversa_id")
    private Conversa conversa;

    private String fromUserId;
    private String text;
    private Instant ts;

    public Mensagem() {}
    public Mensagem(String id){ this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Conversa getConversation() { return conversa; }
    public void setConversation(Conversa conversa) { this.conversa = conversa; }
    public String getFromUserId() { return fromUserId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Instant getTs() { return ts; }
    public void setTs(Instant ts) { this.ts = ts; }
}
