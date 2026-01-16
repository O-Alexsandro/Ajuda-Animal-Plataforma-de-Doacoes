package com.ajudaanimal.doacoes.controller.doacao;

import com.ajudaanimal.doacoes.entity.doacao.AtualizarDoacaoDTO;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoDTO;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoResponseDTO;
import com.ajudaanimal.doacoes.service.impl.DoacaoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/doacoes")
public class DoacaoController {

    @Autowired DoacaoServiceImpl doacaoService;

    @GetMapping
    public ResponseEntity<List<Doacao>> listarDoacoes(){
        return ResponseEntity.ok(doacaoService.listarDoacoes());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Doacao> listarDoacaoPorId(@PathVariable Long id){
        Doacao doacao = doacaoService.listarDoacaoPorId(id);
        return ResponseEntity.ok(doacao);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Doacao>> listarDoacaoPorUsuario(@PathVariable Long idUsuario){
        List<Doacao> responseDTO = doacaoService.listarDoacaoPorUsuario(idUsuario);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/busca/TipoItem/{categoria}")
    public ResponseEntity<List<DoacaoResponseDTO>> listarDoacaoPorTipoDeItem(@PathVariable String categoria){
        List<DoacaoResponseDTO> responseDTO = doacaoService.listarDoacaoPorTipoDeItem(categoria);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/busca/EstadoConservacao/{estado}")
    public ResponseEntity<List<DoacaoResponseDTO>> listarDoacaoPorEstadoConservacao(@PathVariable String estado){
        List<DoacaoResponseDTO> responseDTO = doacaoService.listarDoacaoPorEstadoConservacao(estado);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/busca/estado/{estado}")
    public ResponseEntity<List<DoacaoResponseDTO>> listarDoacaoPorCidade(@PathVariable String estado){
        List<DoacaoResponseDTO> responseDTO = doacaoService.listarDoacaoPorEstado(estado);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<Doacao> criarDoacao(@ModelAttribute @Valid DoacaoDTO doacaoDTO,
                                              @RequestParam("imagem")MultipartFile file) throws IOException {
        doacaoService.criarDoacao(doacaoDTO, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Doacao> atualizarDoacao (@ModelAttribute @Valid AtualizarDoacaoDTO doacaoDTO,
                                                   @RequestParam(name = "imagem", required = false)MultipartFile file) throws IOException {
        Doacao atualizarDoacao = doacaoService.atualizarDoacao(doacaoDTO, file);
        return ResponseEntity.ok(atualizarDoacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletarDoacao(@PathVariable Long id){
        doacaoService.deletarDoacao(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirmar/{idUsuario}/{idDoacao}")
    public ResponseEntity<Doacao> confirmarDoacao(@PathVariable Long idUsuario,
                                                 @PathVariable Long idDoacao){
        Doacao doacaoConfirmada = doacaoService.confirmarDoacao(idUsuario, idDoacao);
        return ResponseEntity.ok(doacaoConfirmada);
    }
}