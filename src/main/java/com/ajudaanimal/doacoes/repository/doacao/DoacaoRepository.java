package com.ajudaanimal.doacoes.repository.doacao;

import com.ajudaanimal.doacoes.entity.doacao.Categoria;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.EstadoConservacao;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    Doacao findByCategoria(Categoria categoriaEnum);
    Doacao findByEstadoConservacao(EstadoConservacao estadoConservacao);
    Doacao findByEstado(String request);

    List<Doacao> findByUsuario(Usuario usuario);
}
