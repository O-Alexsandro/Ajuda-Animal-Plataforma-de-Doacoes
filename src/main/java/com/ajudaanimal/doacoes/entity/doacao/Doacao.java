package com.ajudaanimal.doacoes.entity.doacao;

import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;


@Entity
@Table(name = "doacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private EstadoConservacao estadoConservacao;
    @Lob
    private byte[] imagem;
    private Status status;
    private LocalDateTime dataCadastro;

    public Doacao(DoacaoDTO doacaoDTO, Usuario usuario, MultipartFile file) throws IOException {
        this.usuario = usuario;
        this.titulo = doacaoDTO.titulo();
        this.descricao = doacaoDTO.descricao();
        this.categoria = doacaoDTO.categoria();
        this.estadoConservacao = doacaoDTO.estadoConservacao();
        this.status = Status.DISPONIVEL;
        this.dataCadastro = LocalDateTime.now();
        this.imagem = file.getBytes();
    }
}
