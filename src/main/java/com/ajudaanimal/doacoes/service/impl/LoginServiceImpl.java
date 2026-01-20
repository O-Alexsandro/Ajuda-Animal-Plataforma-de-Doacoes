package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.login.LoginDTO;
import com.ajudaanimal.doacoes.entity.login.ResponseTokenDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TokenServiceImpl tokenService;

    private final AuthenticationManager authenticationManager;

    public LoginServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseTokenDTO login(LoginDTO data){
        var dadosUsuario = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        var auth = authenticationManager.authenticate(dadosUsuario);
        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());
        return new ResponseTokenDTO(token);
    }
}
