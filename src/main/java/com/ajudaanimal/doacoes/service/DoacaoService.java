package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.doacao.AtualizarDoacaoDTO;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoDTO;

import java.util.List;

public interface DoacaoService {

    public List<Doacao> listarDoacoes();
    public Doacao criarDoacao(DoacaoDTO doacaoDTO);
    public Doacao atualizarDoacao(AtualizarDoacaoDTO doacaoDTO);
    public void deletarDoacao(Long id);
}
