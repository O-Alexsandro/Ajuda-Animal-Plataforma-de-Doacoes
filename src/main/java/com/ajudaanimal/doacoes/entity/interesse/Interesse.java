package com.ajudaanimal.doacoes.entity.interesse;

import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "interesse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Interesse {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "doacao_id", nullable = false)
    private Doacao doacao;

    private LocalDateTime dataInteresse;

    @Enumerated(EnumType.STRING)
    private StatusInteresse statusInteresse;

    public Interesse(InteresseDTO interesseDTO, Usuario usuario, Doacao doacao) {
        this.usuario = usuario;
        this.doacao = doacao;
        this.dataInteresse = LocalDateTime.now();
        this.statusInteresse = StatusInteresse.AGUARDANDO_RESPOSTA;
    }
}
