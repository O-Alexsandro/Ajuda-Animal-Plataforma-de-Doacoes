package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.doacao.*;
import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.StatusInteresse;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioResumoDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoacaoServiceImpl implements DoacaoService {

    @Autowired DoacaoRepository doacaoRepository;

    @Autowired UsuarioRepository usuarioRepository;

    @Autowired InteresseRepository interesseRepository;

    @Autowired EmailSenderServiceImpl emailSenderService;

    @Override
    public List<Doacao> listarDoacoes() {
        return doacaoRepository.findAll();
    }

    public Doacao listarDoacaoPorId(Long id) {
        return doacaoRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));
    }

    @Override
    public Doacao criarDoacao(DoacaoDTO doacaoDTO, List<MultipartFile> files) throws IOException {
        Usuario usuario = usuarioRepository.findById(doacaoDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));
        Doacao novaDoacao = new Doacao(doacaoDTO, usuario, files);
        return doacaoRepository.save(novaDoacao);
    }

    @Override
    @Transactional
    public Doacao atualizarDoacao(AtualizarDoacaoDTO doacaoDTO, List<MultipartFile> files) throws IOException {
        // Apenas valida a existência do usuário; o objeto não é necessário aqui.
        usuarioRepository.findById(doacaoDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));

        Doacao doacao = doacaoRepository.findById(doacaoDTO.id()).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));

        // Se não houver arquivos (null ou vazio), apenas não substituímos as imagens;
        // a lógica de manter as imagens atuais fica em `atualizarDadosDoacao`.
        Doacao atualizacao = atualizarDadosDoacao(doacao, doacaoDTO, files);
        return doacaoRepository.save(atualizacao);
    }

    @Override
    @Transactional
    public void deletarDoacao(Long id) {
        Doacao doacao = doacaoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Doação não encontrada"));

        // Busca todos os interesses associados a essa doação
        List<Interesse> interesses = interesseRepository.findByDoacaoId(id);

        // Envia emails de cancelamento para cada usuário interessado (se houver)
        if (interesses != null && !interesses.isEmpty()) {
            emailSenderService.enviarEmailCancelamentoItem(doacao, interesses);
            // Após informar os interessados, remove os registros de interesse
            interesseRepository.deleteAll(interesses);
        }

        doacaoRepository.delete(doacao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorTipoDeItem(String categoria) {
        Categoria categoriaEnum = Categoria.valueOf(categoria.toUpperCase());
        Doacao response = doacaoRepository.findByCategoria(categoriaEnum);
        // usa toDTO para copiar os byte[] enquanto a transação está ativa
        return List.of(toDTO(response));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorEstadoConservacao(String estadoConservacao) {
        EstadoConservacao estadoConservacaoEnum = EstadoConservacao.valueOf(estadoConservacao.toUpperCase());
        Doacao response = doacaoRepository.findByEstadoConservacao(estadoConservacaoEnum);
        return List.of(toDTO(response));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorEstado(String estado) {
        String request = estado.toUpperCase();
        Doacao response = doacaoRepository.findByEstado(request);
        return List.of(toDTO(response));
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

    public Doacao atualizarDadosDoacao(Doacao doacao, AtualizarDoacaoDTO doacaoDTO, List<MultipartFile> files) throws IOException {
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
        if (doacaoDTO.bairro() != null){
            doacao.setBairro(doacaoDTO.bairro());
        }
        if (doacaoDTO.cep() != null){
            doacao.setCep(doacaoDTO.cep());
        }
        // Tratamento seguro dos `files`: pode ser null ou estar vazio.
        if (files != null && !files.isEmpty()){
            // Substituímos todas as imagens atuais pela nova lista enviada
            doacao.getImagens().clear();
            for (MultipartFile f : files) {
                if (f != null && !f.isEmpty()) {
                    doacao.getImagens().add(f.getBytes());
                }
            }
        }
        return doacao;
    }

    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacoesDTO() {
        List<Doacao> all = doacaoRepository.findAll();
        return all.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoacaoResponseDTO listarDoacaoPorIdDTO(Long id) {
        Doacao d = doacaoRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));
        return toDTO(d);
    }

    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacaoPorUsuarioDTO(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));
        List<Doacao> list = doacaoRepository.findByUsuario(usuario);
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> listarDoacoesDisponiveis() {
        List<Doacao> disponiveis = doacaoRepository.findByStatusIn(List.of(Status.DISPONIVEL, Status.EM_ANDAMENTO));
        return disponiveis.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DoacaoResponseDTO toDTO(Doacao response) {
        // Copia os byte[] das imagens enquanto a sessão/transação ainda está ativa
        List<byte[]> imagensCopiadas = new ArrayList<>();
        if (response.getImagens() != null) {
            for (byte[] b : response.getImagens()) {
                if (b != null) {
                    imagensCopiadas.add(java.util.Arrays.copyOf(b, b.length));
                } else {
                    imagensCopiadas.add(null);
                }
            }
        }

        Usuario criador = response.getUsuario();
        UsuarioResumoDTO usuarioResumoDTO = null;
        if (criador != null) {
            usuarioResumoDTO = new UsuarioResumoDTO(
                    criador.getId(),
                    criador.getNome(),
                    criador.getEmail(),
                    criador.getTelefone(),
                    criador.getRua(),
                    criador.getCidade(),
                    criador.getEstado(),
                    criador.getBairro(),
                    criador.getTipoDeConta()
            );
        }

        return new DoacaoResponseDTO(
                response.getId(),
                response.getTitulo(),
                response.getDescricao(),
                response.getCategoria(),
                response.getEstadoConservacao(),
                response.getEstado(),
                response.getCidade(),
                response.getBairro(),
                response.getCep(),
                response.getStatus(),
                response.getDataCadastro(),
                imagensCopiadas,
                usuarioResumoDTO
        );
    }

}
