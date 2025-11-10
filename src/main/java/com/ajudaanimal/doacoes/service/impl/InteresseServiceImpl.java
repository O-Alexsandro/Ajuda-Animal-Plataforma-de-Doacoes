package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.InteresseDTO;
import com.ajudaanimal.doacoes.entity.interesse.InteresseResponseDTO;
import com.ajudaanimal.doacoes.entity.interesse.StatusInteresse;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.repository.doacao.DoacaoRepository;
import com.ajudaanimal.doacoes.repository.interesse.InteresseRepository;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.InteresseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InteresseServiceImpl implements InteresseService {

    @Autowired InteresseRepository interesseRepository;

    @Autowired UsuarioRepository usuarioRepository;

    @Autowired DoacaoRepository doacaoRepository;

    @Override
    public Interesse criarInteresse(InteresseDTO interesseDTO) {
        Usuario usuario = usuarioRepository.findById(interesseDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));

        Doacao doacao = doacaoRepository.findById(interesseDTO.doacaoId()).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));

        Interesse interesse = new Interesse(interesseDTO, usuario, doacao);
        return interesseRepository.save(interesse);
    }

    @Override
    public List<Interesse> listarInteresse() {
        return interesseRepository.findAll();
    }

    @Override
    public Interesse buscarInteressePorId(Long id) {
        return interesseRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Interesse não localizado"));
    }

    @Override
    public Interesse cancelarInteresse(Long idUsuario, Long idInteresse) {
        Interesse interesse = interesseRepository.findById(idInteresse)
                .orElseThrow(() -> new EntityNotFoundException("Interesse não localizado"));

        if (!interesse.getUsuario().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("Este interesse não pertence ao usuário informado");
        }
        interesse.setStatusInteresse(StatusInteresse.CANCELADO);
        interesseRepository.save(interesse);
        return interesse;
    }

    @Override
    public Interesse recusarInteresse(Long idInteresse) {
        Interesse interesse = interesseRepository.findById(idInteresse)
                .orElseThrow(() -> new EntityNotFoundException("Interesse não localizado"));

        interesse.setStatusInteresse(StatusInteresse.RECUSADO);
        interesseRepository.save(interesse);
        return interesse;
    }

    @Override
    @Transactional(readOnly = true)
    public InteresseResponseDTO buscarInteressePorStatus(String statusInteresse) {
        StatusInteresse statusEnum = StatusInteresse.valueOf(statusInteresse.toUpperCase());
        Interesse response = interesseRepository.findByStatusInteresse(statusEnum);

        InteresseResponseDTO usuarioDTO = new InteresseResponseDTO(
                response.getUsuario().getId(),
                response.getUsuario().getNome(),
                response.getUsuario().getEmail(),
                response.getUsuario().getTelefone(),
                response.getUsuario().getTipoDeConta(),
                response.getDoacao().getId(),
                response.getDoacao().getTitulo(),
                response.getDoacao().getDescricao(),
                response.getDoacao().getCategoria(),
                response.getDoacao().getEstadoConservacao(),
                statusEnum,
                response.getDoacao().getDataCadastro()

        );
        return usuarioDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<InteresseResponseDTO> listarInteressePorDoacao(Long idDoacao) {
        List<Interesse> response = interesseRepository.findByDoacaoId(idDoacao);
        return response.stream()
                .map(interesse -> new InteresseResponseDTO(
                        interesse.getUsuario().getId(),
                        interesse.getUsuario().getNome(),
                        interesse.getUsuario().getEmail(),
                        interesse.getUsuario().getTelefone(),
                        interesse.getUsuario().getTipoDeConta(),
                        interesse.getDoacao().getId(),
                        interesse.getDoacao().getTitulo(),
                        interesse.getDoacao().getDescricao(),
                        interesse.getDoacao().getCategoria(),
                        interesse.getDoacao().getEstadoConservacao(),
                        interesse.getStatusInteresse(),
                        interesse.getDoacao().getDataCadastro()
                ))
                .toList();
    }
}