package com.ajudaanimal.doacoes.entity.chat;

import jakarta.persistence.*;

@Entity
@Table(name = "participante")
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversa_id")
    private Conversa conversa;

    private String userId;
    private int unreadCount = 0;

    public Participante() {}
    public Participante(Conversa conversa, String userId){ this.conversa = conversa; this.userId = userId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Conversa getConversation() { return conversa; }
    public void setConversation(Conversa conversa) { this.conversa = conversa; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getUnreadCount() { return unreadCount; }
    public void setUnreadCount(int unreadCount) { this.unreadCount = unreadCount; }
}
