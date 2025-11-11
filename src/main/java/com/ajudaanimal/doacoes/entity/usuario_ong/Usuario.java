package com.ajudaanimal.doacoes.entity.usuario_ong;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Table
@Entity(name="usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Usuario implements UserDetails {
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
    private LocalDateTime dataCadastro;

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
        this.dataCadastro = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        switch (tipoDeConta) {
            case USUARIO -> authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
            case ONG -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_ONG"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
            }
            case ADMIN -> {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                authorities.add(new SimpleGrantedAuthority("ROLE_ONG"));
                authorities.add(new SimpleGrantedAuthority("ROLE_USUARIO"));
            }
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
