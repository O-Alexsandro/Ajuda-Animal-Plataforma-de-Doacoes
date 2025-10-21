package com.ajudaanimal.doacoes.controller.doacao;

import com.ajudaanimal.doacoes.entity.doacao.AtualizarDoacaoDTO;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoDTO;
import com.ajudaanimal.doacoes.service.impl.DoacaoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    @Autowired DoacaoServiceImpl doacaoService;

    @GetMapping
    public ResponseEntity<List<Doacao>> listarDoacoes(){
        return ResponseEntity.ok(doacaoService.listarDoacoes());
    }

    @PostMapping
    public ResponseEntity<Doacao> criarDoacao(@RequestBody @Valid DoacaoDTO doacaoDTO){
        doacaoService.criarDoacao(doacaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Doacao> atualizarDoacao (@RequestBody @Valid AtualizarDoacaoDTO doacaoDTO){
        Doacao atualizarDoacao = doacaoService.atualizarDoacao(doacaoDTO);
        return ResponseEntity.ok(atualizarDoacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarDoacao(@PathVariable Long id){
        doacaoService.deletarDoacao(id);
        return ResponseEntity.noContent().build();
    }
}
