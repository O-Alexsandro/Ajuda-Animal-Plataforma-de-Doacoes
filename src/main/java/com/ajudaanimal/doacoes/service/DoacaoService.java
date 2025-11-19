package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.doacao.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DoacaoService {

    public List<Doacao> listarDoacoes();
    public Doacao criarDoacao(DoacaoDTO doacaoDTO, MultipartFile file) throws IOException;
    public Doacao atualizarDoacao(AtualizarDoacaoDTO doacaoDTO, MultipartFile file) throws IOException;
    public void deletarDoacao(Long id);
    public List<DoacaoResponseDTO> listarDoacaoPorTipoDeItem(String categoria);
    public List<DoacaoResponseDTO> listarDoacaoPorEstadoConservacao(String estadoConservacao);
    public List<DoacaoResponseDTO> listarDoacaoPorEstado(String estado);
}
