package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;

import java.time.Instant;

public interface TokenService {
    String gerarToken(Usuario usuario);
    String validarToken(String token);
    Instant expiracaoToken();
}
