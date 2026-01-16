package com.ajudaanimal.doacoes.repository.interesse;

import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.StatusInteresse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteresseRepository extends JpaRepository<Interesse, Long> {
    Interesse findByStatusInteresse(StatusInteresse statusInteresse);

    List<Interesse> findByDoacaoId(Long idAdocao);

    List<Interesse> findAllByUsuarioId(Long idUsuario);

    // New method to quickly check whether a user already expressed interest in a donation
    boolean existsByUsuarioIdAndDoacaoId(Long usuarioId, Long doacaoId);
}
