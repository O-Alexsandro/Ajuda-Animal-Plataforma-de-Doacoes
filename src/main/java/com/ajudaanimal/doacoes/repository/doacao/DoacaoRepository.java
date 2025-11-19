package com.ajudaanimal.doacoes.repository.doacao;

import com.ajudaanimal.doacoes.entity.doacao.Categoria;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.EstadoConservacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    Doacao findByCategoria(Categoria categoriaEnum);
    Doacao findByEstadoConservacao(EstadoConservacao estadoConservacao);
    Doacao findByEstado(String request);
}
