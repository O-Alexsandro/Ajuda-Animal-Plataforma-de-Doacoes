package com.ajudaanimal.doacoes.entity.chat;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Conversa {
    @Id
    private String id; // keep simple string ids (UUID recommended)
    private Long donationId;
    private String lastMessage;
    private Instant lastMessageTs;

    @OneToMany(mappedBy = "conversa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Participante> participants = new HashSet<>();

    public Conversa() {}
    public Conversa(String id){ this.id = id; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Long getDonationId() { return donationId; }
    public void setDonationId(Long donationId) { this.donationId = donationId; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public Instant getLastMessageTs() { return lastMessageTs; }
    public void setLastMessageTs(Instant lastMessageTs) { this.lastMessageTs = lastMessageTs; }
    public Set<Participante> getParticipants() { return participants; }
    public void setParticipants(Set<Participante> participants) { this.participants = participants; }
}
