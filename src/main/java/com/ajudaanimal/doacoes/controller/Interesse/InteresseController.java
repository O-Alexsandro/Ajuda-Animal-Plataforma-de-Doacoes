package com.ajudaanimal.doacoes.controller.Interesse;

import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.InteresseDTO;
import com.ajudaanimal.doacoes.entity.interesse.InteresseResponseDTO;
import com.ajudaanimal.doacoes.service.impl.InteresseServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interesse")
public class InteresseController {

    @Autowired InteresseServiceImpl interesseService;

    @PostMapping
    public ResponseEntity<InteresseResponseDTO> criarInteresse(@RequestBody @Valid InteresseDTO interesseDTO){
        Interesse criado = interesseService.criarInteresse(interesseDTO);
        InteresseResponseDTO dto = interesseService.buscarInteressePorIdDTO(criado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<InteresseResponseDTO>> listarInteresses(){
        return ResponseEntity.ok(interesseService.listarInteresseDTO());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InteresseResponseDTO> buscarInteressePorId(@PathVariable Long id){
        InteresseResponseDTO interesse = interesseService.buscarInteressePorIdDTO(id);
        return ResponseEntity.ok(interesse);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<InteresseResponseDTO>> buscarInteressePorUsuario(@PathVariable Long idUsuario){
        List<InteresseResponseDTO> interesse = interesseService.buscarInteressePorUsuario(idUsuario);
        return ResponseEntity.ok(interesse);
    }

    // Usuario cancela o interesse pelo item, sumindo da tela de histórico.
    @DeleteMapping("cancelar/{idUsuario}/{idInteresse}")
    public ResponseEntity<Void> cancelarInteresse(@PathVariable Long idUsuario,
                                                       @PathVariable Long idInteresse){
        interesseService.cancelarInteresse(idUsuario, idInteresse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/doacao/{idDoacao}")
    public ResponseEntity<List<InteresseResponseDTO>> buscarInteressePorDoacao(@PathVariable Long idDoacao){
        return ResponseEntity.ok(interesseService.listarInteressePorDoacao(idDoacao));
    }

    @GetMapping("/status/{statusInteresse}")
    public ResponseEntity<InteresseResponseDTO> buscarInteressePorStatus(@PathVariable String statusInteresse){
        return ResponseEntity.ok(interesseService.buscarInteressePorStatus(statusInteresse));
    }

}