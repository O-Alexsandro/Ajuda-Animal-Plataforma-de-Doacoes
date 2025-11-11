package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.login.LoginDTO;
import com.ajudaanimal.doacoes.entity.login.ResponseTokenDTO;

public interface LoginService {

    ResponseTokenDTO login(LoginDTO login);

}
