package com.ajudaanimal.doacoes.entity.usuario_ong;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Table
@Entity(name="usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private String rua;
    private String cidade;
    private String estado;
    private String bairro;
    private TipoDeConta tipoDeConta;
    private LocalDate dataCadastro;

    public Usuario(UsuarioDTO usuarioDTO) {
        this.nome = usuarioDTO.nome();
        this.email = usuarioDTO.email();
        this.senha = usuarioDTO.senha();
        this.telefone = usuarioDTO.telefone();
        this.rua = usuarioDTO.rua();
        this.cidade = usuarioDTO.cidade();
        this.estado = usuarioDTO.estado();
        this.bairro = usuarioDTO.bairro();
        this.tipoDeConta = usuarioDTO.tipoDeConta();
        this.dataCadastro = LocalDate.now();
    }
}
