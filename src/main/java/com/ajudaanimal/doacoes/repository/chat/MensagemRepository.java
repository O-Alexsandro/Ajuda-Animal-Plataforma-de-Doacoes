package com.ajudaanimal.doacoes.repository.chat;

import com.ajudaanimal.doacoes.entity.chat.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensagemRepository extends JpaRepository<Mensagem, String> {

    List<Mensagem> findByConversa_IdOrderByTsAsc(String conversaId);

}
