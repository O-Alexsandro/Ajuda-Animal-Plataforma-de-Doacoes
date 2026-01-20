package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.usuario_ong.AtualizarUsuarioDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.ResetSenhaDTO;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {
    @Autowired UsuarioRepository usuarioRepository;

    @Autowired EmailSenderServiceImpl emailSenderService;

    @Override
    public Usuario buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuário não encontrado"));
        return usuario;
    }

    @Override
    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario(usuarioDTO);
        String senhaBcrypt = new BCryptPasswordEncoder().encode(usuarioDTO.senha());
        usuario.setSenha(senhaBcrypt);
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

    @Override
    @Transactional
    public void resetSenha(ResetSenhaDTO resetSenhaDTO) {
        if (resetSenhaDTO == null) {
            throw new IllegalArgumentException("Dados de reset não podem ser nulos");
        }

        String email = resetSenhaDTO.email();
        String nome = resetSenhaDTO.nome();
        String novaSenha = resetSenhaDTO.novaSenha();

        UserDetails userDetails = usuarioRepository.findByEmail(email);
        if (userDetails == null) {
            // Por segurança, não revelar se o usuário existe - lançar exceção genérica
            throw new RuntimeException("Não foi possível resetar a senha");
        }

        Usuario usuario = (Usuario) userDetails;

        if (!usuario.getNome().trim().equalsIgnoreCase(nome)) {
            throw new RuntimeException("Nome e email não correspondem");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(novaSenha, usuario.getSenha())) {
            throw new RuntimeException("A nova senha não pode ser igual à senha antiga");
        }

        String senhaBcrypt = encoder.encode(novaSenha);
        usuario.setSenha(senhaBcrypt);
        usuarioRepository.save(usuario);
    }

    public Usuario atualizarDados(Usuario usuario, AtualizarUsuarioDTO dados){
        if (dados.nome() != null){
            usuario.setNome(dados.nome());
        }
        if (dados.email() != null){
            usuario.setEmail(dados.email());
        }
        if (dados.senha() != null) {
            String senhaBcrypt = new BCryptPasswordEncoder().encode(dados.senha());
            usuario.setSenha(senhaBcrypt);        }
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }
}
