package com.ajudaanimal.doacoes.controller.usuarios;

import com.ajudaanimal.doacoes.entity.usuario_ong.AtualizarUsuarioDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioDTO;
import com.ajudaanimal.doacoes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/usuarios")
@RestController
public class UsuarioController {

    @Autowired UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario (@RequestBody @Valid UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioService.criarUsuario(usuarioDTO);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    public ResponseEntity<Usuario> atualizarUsuario(@RequestBody @Valid AtualizarUsuarioDTO atualizarUsuarioDTO){
        Usuario usuario = usuarioService.atualizarUsuario(atualizarUsuarioDTO);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Usuario> deletarUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.deletarUsuario(id));
    }
}