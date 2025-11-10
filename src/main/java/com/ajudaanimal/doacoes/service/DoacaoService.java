package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.doacao.AtualizarDoacaoDTO;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DoacaoService {

    public List<Doacao> listarDoacoes();
    public Doacao criarDoacao(DoacaoDTO doacaoDTO, MultipartFile file) throws IOException;
    public Doacao atualizarDoacao(AtualizarDoacaoDTO doacaoDTO, MultipartFile file) throws IOException;
    public void deletarDoacao(Long id);
}
