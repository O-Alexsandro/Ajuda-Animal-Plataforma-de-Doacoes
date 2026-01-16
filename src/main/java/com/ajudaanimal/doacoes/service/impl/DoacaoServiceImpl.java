package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.doacao.*;
import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.StatusInteresse;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.repository.doacao.DoacaoRepository;
import com.ajudaanimal.doacoes.repository.interesse.InteresseRepository;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.DoacaoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DoacaoServiceImpl implements DoacaoService {

    @Autowired DoacaoRepository doacaoRepository;

    @Autowired UsuarioRepository usuarioRepository;

    @Autowired InteresseRepository interesseRepository;
    @Override
    public List<Doacao> listarDoacoes() {
        return doacaoRepository.findAll();
    }

    public Doacao listarDoacaoPorId(Long id) {
        return doacaoRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));
    }

    @Override
    public Doacao criarDoacao(DoacaoDTO doacaoDTO, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepository.findById(doacaoDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));
        Doacao novaDoacao = new Doacao(doacaoDTO, usuario,file);
        return doacaoRepository.save(novaDoacao);
    }

    @Override
    public Doacao atualizarDoacao(AtualizarDoacaoDTO doacaoDTO, MultipartFile file) throws IOException {
        // Apenas valida a existência do usuário; o objeto não é necessário aqui.
        usuarioRepository.findById(doacaoDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));

        Doacao doacao = doacaoRepository.findById(doacaoDTO.id()).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));

        // Se não houver arquivo (null ou vazio), apenas não substituímos a imagem;
        // a lógica de manter a imagem atual fica em `atualizarDadosDoacao`.
        Doacao atualizacao = atualizarDadosDoacao(doacao, doacaoDTO, file);
        return doacaoRepository.save(atualizacao);
    }

    @Override
    public void deletarDoacao(Long id) {
        Doacao doacao = doacaoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Doação não encontrada"));
        doacaoRepository.delete(doacao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorTipoDeItem(String categoria) {
        Categoria categoriaEnum = Categoria.valueOf(categoria.toUpperCase());
        Doacao response = doacaoRepository.findByCategoria(categoriaEnum);

        DoacaoResponseDTO doacaoResponseDTO = new DoacaoResponseDTO(
                response.getTitulo(),
                response.getDescricao(),
                response.getCategoria(),
                response.getEstadoConservacao(),
                response.getEstado(),
                response.getCidade(),
                response.getImagem()
        );
        return List.of(doacaoResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorEstadoConservacao(String estadoConservacao) {
        EstadoConservacao estadoConservacaoEnum = EstadoConservacao.valueOf(estadoConservacao.toUpperCase());
        Doacao response = doacaoRepository.findByEstadoConservacao(estadoConservacaoEnum);

        DoacaoResponseDTO doacaoResponseDTO = new DoacaoResponseDTO(
                response.getTitulo(),
                response.getDescricao(),
                response.getCategoria(),
                response.getEstadoConservacao(),
                response.getEstado(),
                response.getCidade(),
                response.getImagem()
        );
        return List.of(doacaoResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorEstado(String estado) {
        String request = estado.toUpperCase();
        Doacao response = doacaoRepository.findByEstado(request);

        DoacaoResponseDTO doacaoResponseDTO = new DoacaoResponseDTO(
                response.getTitulo(),
                response.getDescricao(),
                response.getCategoria(),
                response.getEstadoConservacao(),
                response.getEstado(),
                response.getCidade(),
                response.getImagem()
        );
        return List.of(doacaoResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Doacao> listarDoacaoPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));
        return doacaoRepository.findByUsuario(usuario);
    }

    @Override
    @Transactional
    public Doacao confirmarDoacao(Long idUsuario, Long idDoacao) {
        usuarioRepository.findById(idUsuario).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));

        Doacao doacao = doacaoRepository.findById(idDoacao).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));

        // Buscar todos os interesses relacionados a essa doação
        List<Interesse> interesses = interesseRepository.findByDoacaoId(idDoacao);

        // Atualizar status dos interesses: o usuário que confirmou -> CONCLUIDO; os demais -> RECUSADO
        for (Interesse interesse : interesses) {
            if (interesse.getUsuario().getId().equals(idUsuario)) {
                interesse.setStatusInteresse(StatusInteresse.CONCLUIDO);
            } else {
                interesse.setStatusInteresse(StatusInteresse.RECUSADO);
            }
        }

        // Persistir as mudanças nos interesses
        interesseRepository.saveAll(interesses);

        // Atualizar status da doação para CONCLUIDO
        doacao.setStatus(Status.CONCLUIDO);
        doacaoRepository.save(doacao);

        return doacao;
    }

    public Doacao atualizarDadosDoacao(Doacao doacao, AtualizarDoacaoDTO doacaoDTO, MultipartFile file) throws IOException {
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
        if (doacaoDTO.estado() != null){
            doacao.setEstado(doacaoDTO.estado());
        }
        if (doacaoDTO.cidade() != null){
            doacao.setCidade(doacaoDTO.cidade());
        }
        // Tratamento seguro do `file`: pode ser null ou estar vazio.
        if (file != null && !file.isEmpty()){
            byte[] imagem = file.getBytes();
            doacao.setImagem(imagem);
        }
        return doacao;
    }


}

