package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.doacao.AtualizarDoacaoDTO;
import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.repository.doacao.DoacaoRepository;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.DoacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoacaoServiceImpl implements DoacaoService {

    @Autowired DoacaoRepository doacaoRepository;

    @Autowired UsuarioRepository usuarioRepository;
    @Override
    public List<Doacao> listarDoacoes() {
        return doacaoRepository.findAll();
    }

    @Override
    public Doacao criarDoacao(DoacaoDTO doacaoDTO) {
        Usuario usuario = usuarioRepository.findById(doacaoDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));
        Doacao novaDoacao = new Doacao(doacaoDTO, usuario);
        return doacaoRepository.save(novaDoacao);
    }

    @Override
    public Doacao atualizarDoacao(AtualizarDoacaoDTO doacaoDTO) {
        Usuario usuario = usuarioRepository.findById(doacaoDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));

        Doacao doacao = doacaoRepository.findById(doacaoDTO.id()).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));

        Doacao atualizacao = atualizarDadosDoacao(doacao, doacaoDTO);
        return doacaoRepository.save(atualizacao);
    }

    @Override
    public void deletarDoacao(Long id) {
        Doacao doacao = doacaoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Doação não encontrada"));
        doacaoRepository.delete(doacao);
    }

    public Doacao atualizarDadosDoacao(Doacao doacao, AtualizarDoacaoDTO doacaoDTO){
        if (doacaoDTO.titulo() != null){
            doacao.setTitulo(doacaoDTO.titulo());
        }
        if (doacaoDTO.descricao() != null){
            doacao.setDescricao(doacaoDTO.descricao());
        }
        if (doacaoDTO.categoria() != null){
            doacao.setCategoria(doacaoDTO.categoria());
        }
        if (doacaoDTO.estadoConservacao() != null){
            doacao.setEstadoConservacao(doacaoDTO.estadoConservacao());
        }

        return doacao;
    }
}
