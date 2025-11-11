package com.ajudaanimal.doacoes.controller.login;

import com.ajudaanimal.doacoes.entity.login.LoginDTO;
import com.ajudaanimal.doacoes.entity.login.ResponseTokenDTO;
import com.ajudaanimal.doacoes.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginServiceImpl loginService;

    @PostMapping
    public ResponseEntity<ResponseTokenDTO> login(@RequestBody LoginDTO login){
        var response = loginService.login(login);
        return ResponseEntity.ok(response);
    }

}
