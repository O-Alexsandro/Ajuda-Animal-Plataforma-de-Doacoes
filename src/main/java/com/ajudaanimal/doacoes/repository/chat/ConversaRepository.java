package com.ajudaanimal.doacoes.repository.chat;

import com.ajudaanimal.doacoes.entity.chat.Conversa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversaRepository extends JpaRepository<Conversa, String> {
}
