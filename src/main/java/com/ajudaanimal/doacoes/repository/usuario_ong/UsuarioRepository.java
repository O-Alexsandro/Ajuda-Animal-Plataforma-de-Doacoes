package com.ajudaanimal.doacoes.repository.usuario_ong;

import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String userName);
}
