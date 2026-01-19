package com.ajudaanimal.doacoes.entity.doacao;

import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private String estado;
    private String cidade;
    private String bairro;
    private String cep;
    // Armazena várias imagens (cada elemento é um byte[] com o conteúdo do arquivo)
    @ElementCollection
    @CollectionTable(name = "doacao_imagens", joinColumns = @JoinColumn(name = "doacao_id"))
    @Lob
    @Column(name = "imagem")
    private List<byte[]> imagens = new ArrayList<>();
    private Status status;
    private LocalDateTime dataCadastro;

    // Construtor que aceita múltiplos arquivos
    public Doacao(DoacaoDTO doacaoDTO, Usuario usuario, List<MultipartFile> files) throws IOException {
        this.usuario = usuario;
        this.titulo = doacaoDTO.titulo();
        this.descricao = doacaoDTO.descricao();
        this.categoria = doacaoDTO.categoria();
        this.estadoConservacao = doacaoDTO.estadoConservacao();
        this.estado = doacaoDTO.estado();
        this.cidade = doacaoDTO.cidade();
        this.bairro = doacaoDTO.bairro();
        this.cep = doacaoDTO.cep();
        this.status = Status.DISPONIVEL;
        this.dataCadastro = LocalDateTime.now();
        if (files != null) {
            for (MultipartFile f : files) {
                if (f != null && !f.isEmpty()) {
                    this.imagens.add(f.getBytes());
                }
            }
        }
    }

    // Compatibilidade: construtor que recebe um único arquivo
    public Doacao(DoacaoDTO doacaoDTO, Usuario usuario, MultipartFile file) throws IOException {
        this(doacaoDTO, usuario, file == null ? Collections.emptyList() : Collections.singletonList(file));
    }
}