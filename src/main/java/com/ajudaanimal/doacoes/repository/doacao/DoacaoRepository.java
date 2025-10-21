package com.ajudaanimal.doacoes.repository.doacao;

import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
}
