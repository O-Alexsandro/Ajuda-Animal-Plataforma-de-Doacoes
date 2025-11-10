package com.ajudaanimal.doacoes.controller.Interesse;

import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.InteresseDTO;
import com.ajudaanimal.doacoes.entity.interesse.InteresseResponseDTO;
import com.ajudaanimal.doacoes.entity.interesse.StatusInteresse;
import com.ajudaanimal.doacoes.service.impl.InteresseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interesse")
public class InteresseController {

    @Autowired InteresseServiceImpl interesseService;

    @PostMapping
    public ResponseEntity<Interesse> criarInteresse(@RequestBody @Valid InteresseDTO interesseDTO){
        return ResponseEntity.ok(interesseService.criarInteresse(interesseDTO));
    }

    @GetMapping
    public ResponseEntity<List<Interesse>> listarInteresses(){
        return ResponseEntity.ok(interesseService.listarInteresse());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Interesse> buscarInteressePorId(@PathVariable Long id){
        Interesse interesse = interesseService.buscarInteressePorId(id);
        return ResponseEntity.ok(interesse);
    }

    @PutMapping("cancelar/{idUsuario}/{idInteresse}")
    public ResponseEntity<Interesse> cancelarInteresse(@PathVariable Long idUsuario,
                                                       @PathVariable Long idInteresse){
        Interesse interesse = interesseService.cancelarInteresse(idUsuario, idInteresse);
        return ResponseEntity.ok(interesse);
    }

    @GetMapping("/status/adocao/{idDoacao}")
    public ResponseEntity<List<InteresseResponseDTO>> buscarInteressePorDoacao(@PathVariable Long idDoacao){
        return ResponseEntity.ok(interesseService.listarInteressePorDoacao(idDoacao));
    }

    @GetMapping("/status/{statusInteresse}")
    public ResponseEntity<InteresseResponseDTO> buscarInteressePorStatus(@PathVariable String statusInteresse){
        return ResponseEntity.ok(interesseService.buscarInteressePorStatus(statusInteresse));
    }
}