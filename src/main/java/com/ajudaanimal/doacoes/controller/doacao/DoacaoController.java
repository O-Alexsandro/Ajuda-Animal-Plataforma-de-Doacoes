package com.ajudaanimal.doacoes.controller.doacao;

import com.ajudaanimal.doacoes.entity.doacao.AtualizarDoacaoDTO;
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
    public ResponseEntity<List<DoacaoResponseDTO>> listarDoacoes(){
        return ResponseEntity.ok(doacaoService.listarDoacoesDTO());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DoacaoResponseDTO> listarDoacaoPorId(@PathVariable Long id){
        DoacaoResponseDTO doacao = doacaoService.listarDoacaoPorIdDTO(id);
        return ResponseEntity.ok(doacao);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DoacaoResponseDTO>> listarDoacaoPorUsuario(@PathVariable Long idUsuario){
        List<DoacaoResponseDTO> responseDTO = doacaoService.listarDoacaoPorUsuarioDTO(idUsuario);
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
    public ResponseEntity<Void> criarDoacao(@ModelAttribute @Valid DoacaoDTO doacaoDTO,
                                              @RequestParam(name = "imagens", required = false) List<MultipartFile> imagens) throws IOException {
        doacaoService.criarDoacao(doacaoDTO, imagens);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Void> atualizarDoacao (@ModelAttribute @Valid AtualizarDoacaoDTO doacaoDTO,
                                                   @RequestParam(name = "imagens", required = false) List<MultipartFile> imagens) throws IOException {
        doacaoService.atualizarDoacao(doacaoDTO, imagens);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDoacao(@PathVariable Long id){
        doacaoService.deletarDoacao(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirmar/{idUsuario}/{idDoacao}")
    public ResponseEntity<Void> confirmarDoacao(@PathVariable Long idUsuario,
                                                 @PathVariable Long idDoacao){
        doacaoService.confirmarDoacao(idUsuario, idDoacao);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<DoacaoResponseDTO>> listarDoacoesDisponiveis(){
        return ResponseEntity.ok(doacaoService.listarDoacoesDisponiveis());
    }
}