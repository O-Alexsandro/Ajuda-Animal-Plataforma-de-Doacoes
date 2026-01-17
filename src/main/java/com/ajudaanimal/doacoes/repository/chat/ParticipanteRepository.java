package com.ajudaanimal.doacoes.repository.chat;

import com.ajudaanimal.doacoes.entity.chat.Participante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
    List<Participante> findByUserId(String userId);
    // find participants for a specific conversation id without loading Conversa.participants
    List<Participante> findByConversa_Id(String conversaId);
}
