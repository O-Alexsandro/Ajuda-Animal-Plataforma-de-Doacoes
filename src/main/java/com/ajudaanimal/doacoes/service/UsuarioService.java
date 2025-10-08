package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.usuario_ong.AtualizarUsuarioDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    public Usuario buscarUsuarioPorId(Long id);
    public Usuario criarUsuario(UsuarioDTO usuario);
    public Usuario atualizarUsuario(AtualizarUsuarioDTO atualizarUsuarioDTO);
    public Usuario deletarUsuario(Long id);
    public List<Usuario> listarUsuarios();
}