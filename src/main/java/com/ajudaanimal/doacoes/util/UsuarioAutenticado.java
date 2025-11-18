package com.ajudaanimal.doacoes.util;

import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticado {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario usuarioAutenticado(){

        var auth = SecurityContextHolder.getContext().getAuthentication();
        var email = auth.getName();

        var userDetails = usuarioRepository.findByEmail(email);

        Usuario usuario = (Usuario) userDetails;
        if (usuario == null){
            throw new UsernameNotFoundException("Usuário não está autenticado.");
        }
        return usuario;
    }
}