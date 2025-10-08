package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.usuario_ong.AtualizarUsuarioDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioDTO;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired UsuarioRepository usuarioRepository;

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        return usuario;
    }

    @Override
    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario atualizarUsuario(AtualizarUsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioDTO.id()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (usuario != null){
            atualizarDados(usuario, usuarioDTO);
            usuarioRepository.save(usuario);
        }
        return usuario;
    }

    @Override
    public Usuario deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        if (usuario != null){
            usuarioRepository.delete(usuario);
        } else {
            throw new RuntimeException("Usuário não encontrado");
        }
        return usuario;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario atualizarDados(Usuario usuario, AtualizarUsuarioDTO dados){
        if (dados.nome() != null){
            usuario.setNome(dados.nome());
        }
        if (dados.email() != null){
            usuario.setEmail(dados.email());
        }
        if (dados.senha() != null) {
            usuario.setSenha(dados.senha());
        }
        if (dados.telefone() != null){
            usuario.setTelefone(dados.telefone());
        }
        if (dados.rua() != null) {
            usuario.setRua(dados.rua());
        }
        if (dados.cidade() != null){
            usuario.setCidade(dados.cidade());
        }
        if (dados.estado() != null){
            usuario.setEstado(dados.estado());
        }
        if (dados.bairro() != null){
            usuario.setBairro(dados.bairro());
        }
        return usuario;
    }
}
