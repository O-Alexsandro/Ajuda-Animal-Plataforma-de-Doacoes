package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.doacao.DoacaoResumoDTO;
import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.InteresseDTO;
import com.ajudaanimal.doacoes.entity.interesse.InteresseResponseDTO;
import com.ajudaanimal.doacoes.entity.interesse.StatusInteresse;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioResumoDTO;
import com.ajudaanimal.doacoes.repository.doacao.DoacaoRepository;
import com.ajudaanimal.doacoes.repository.interesse.InteresseRepository;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.InteresseService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.ajudaanimal.doacoes.entity.doacao.Status.EM_ANDAMENTO;

@Service
@Slf4j
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

        // Verifica se o usuário já manifestou interesse nesta doação
        boolean alreadyInterested = interesseRepository.existsByUsuarioIdAndDoacaoId(usuario.getId(), doacao.getId());
        if (alreadyInterested) {
            log.info("O usuario já manifestou interesse na doação de id: " + doacao.getId());
            throw new EntityExistsException("Você já manifestou interesse nesta doação.");
        }

        Interesse interesse = new Interesse(interesseDTO, usuario, doacao);

        doacao.setStatus(EM_ANDAMENTO);
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
    @Transactional(readOnly = true)
    public List<InteresseResponseDTO> buscarInteressePorUsuario(Long idUsuario) {
        List<Interesse> response = interesseRepository.findAllByUsuarioId(idUsuario);
        return response.stream()
                .map(interesse -> {
                    var usuario = interesse.getUsuario();
                    var doacao = interesse.getDoacao();

                    var doacaoResumo = new DoacaoResumoDTO(
                            doacao.getId(),
                            doacao.getTitulo(),
                            doacao.getDescricao(),
                            doacao.getCategoria(),
                            doacao.getEstadoConservacao(),
                            doacao.getEstado(),
                            doacao.getCidade(),
                            doacao.getStatus(),
                            // copia das imagens enquanto a transação está ativa
                            doacao.getImagens() == null ? java.util.List.of() : doacao.getImagens().stream().map(b -> b == null ? null : java.util.Arrays.copyOf(b, b.length)).toList(),
                            doacao.getDataCadastro()
                    );

                    var criador = doacao.getUsuario();
                    UsuarioResumoDTO criadorResumo = null;
                    if (criador != null) {
                        criadorResumo = new UsuarioResumoDTO(
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

                    return new InteresseResponseDTO(
                            interesse.getId(),
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getTelefone(),
                            usuario.getTipoDeConta(),
                            doacaoResumo,
                            criadorResumo,
                            interesse.getStatusInteresse(),
                            interesse.getDataInteresse()
                    );
                })
                .toList();
    }

    @Override
    public Interesse cancelarInteresse(Long idUsuario, Long idInteresse) {
        Interesse interesse = interesseRepository.findById(idInteresse)
                .orElseThrow(() -> new EntityNotFoundException("Interesse não localizado"));

        if (!interesse.getUsuario().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("Este interesse não pertence ao usuário informado");
        }
        interesseRepository.delete(interesse);
        return interesse;
    }

    @Override
    public Interesse recusarInteresse(Long idInteresse) {
        // antigo método - substituído pela nova assinatura
        throw new UnsupportedOperationException("Use recusarInteresse(idUsuario, idInteresse, idDoacao)");
    }

    @Override
    public Interesse recusarInteresse(Long idUsuario, Long idInteresse, Long idDoacao) {
        // valida se a doação existe e pertence ao usuário (idUsuario é o dono da doação)
        var doacao = doacaoRepository.findById(idDoacao).orElseThrow(() -> new EntityNotFoundException("Doação não localizada"));
        if (doacao.getUsuario() == null || !doacao.getUsuario().getId().equals(idUsuario)) {
            throw new IllegalArgumentException("Usuário não autorizado a recusar este interesse");
        }

        // valida se o interesse existe e pertence a essa doação
        var interesse = interesseRepository.findById(idInteresse).orElseThrow(() -> new EntityNotFoundException("Interesse não localizado"));
        if (interesse.getDoacao() == null || !interesse.getDoacao().getId().equals(idDoacao)) {
            throw new IllegalArgumentException("Interesse não pertence à doação informada");
        }

        // atualiza status
        interesse.setStatusInteresse(StatusInteresse.RECUSADO);
        interesseRepository.save(interesse);
        return interesse;
    }

    @Override
    @Transactional(readOnly = true)
    public InteresseResponseDTO buscarInteressePorStatus(String statusInteresse) {
        StatusInteresse statusEnum = StatusInteresse.valueOf(statusInteresse.toUpperCase());
        Interesse response = interesseRepository.findByStatusInteresse(statusEnum);

        var usuario = response.getUsuario();
        var doacao = response.getDoacao();

        var doacaoResumo = new DoacaoResumoDTO(
                doacao.getId(),
                doacao.getTitulo(),
                doacao.getDescricao(),
                doacao.getCategoria(),
                doacao.getEstadoConservacao(),
                doacao.getEstado(),
                doacao.getCidade(),
                doacao.getStatus(),
                doacao.getImagens() == null ? java.util.List.of() : doacao.getImagens().stream().map(b -> b == null ? null : java.util.Arrays.copyOf(b, b.length)).toList(),
                doacao.getDataCadastro()
        );

        var criador = doacao.getUsuario();
        UsuarioResumoDTO criadorResumo = null;
        if (criador != null) {
            criadorResumo = new UsuarioResumoDTO(
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

        return new InteresseResponseDTO(
                response.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipoDeConta(),
                doacaoResumo,
                criadorResumo,
                response.getStatusInteresse(),
                response.getDataInteresse()

        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<InteresseResponseDTO> listarInteressePorDoacao(Long idDoacao) {
        List<Interesse> response = interesseRepository.findByDoacaoId(idDoacao);
        return response.stream()
                .map(interesse -> {
                    var usuario = interesse.getUsuario();
                    var doacao = interesse.getDoacao();

                    var doacaoResumo = new DoacaoResumoDTO(
                            doacao.getId(),
                            doacao.getTitulo(),
                            doacao.getDescricao(),
                            doacao.getCategoria(),
                            doacao.getEstadoConservacao(),
                            doacao.getEstado(),
                            doacao.getCidade(),
                            doacao.getStatus(),
                            doacao.getImagens() == null ? java.util.List.of() : doacao.getImagens().stream().map(b -> b == null ? null : java.util.Arrays.copyOf(b, b.length)).toList(),
                            doacao.getDataCadastro()
                    );

                    var criador = doacao.getUsuario();
                    UsuarioResumoDTO criadorResumo = null;
                    if (criador != null) {
                        criadorResumo = new UsuarioResumoDTO(
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

                    return new InteresseResponseDTO(
                            interesse.getId(),
                            usuario.getId(),
                            usuario.getNome(),
                            usuario.getEmail(),
                            usuario.getTelefone(),
                            usuario.getTipoDeConta(),
                            doacaoResumo,
                            criadorResumo,
                            interesse.getStatusInteresse(),
                            interesse.getDataInteresse()
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<InteresseResponseDTO> listarInteresseDTO() {
        List<Interesse> all = interesseRepository.findAll();
        return all.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InteresseResponseDTO buscarInteressePorIdDTO(Long id) {
        Interesse interesse = interesseRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException("Interesse não localizado"));
        return mapToResponse(interesse);
    }

    private InteresseResponseDTO mapToResponse(Interesse interesse) {
        var usuario = interesse.getUsuario();
        var doacao = interesse.getDoacao();

        var doacaoResumo = new com.ajudaanimal.doacoes.entity.doacao.DoacaoResumoDTO(
                doacao.getId(),
                doacao.getTitulo(),
                doacao.getDescricao(),
                doacao.getCategoria(),
                doacao.getEstadoConservacao(),
                doacao.getEstado(),
                doacao.getCidade(),
                doacao.getStatus(),
                doacao.getImagens() == null ? java.util.List.of() : doacao.getImagens().stream().map(b -> b == null ? null : java.util.Arrays.copyOf(b, b.length)).toList(),
                doacao.getDataCadastro()
        );

        var criador = doacao.getUsuario();
        com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioResumoDTO criadorResumo = null;
        if (criador != null) {
            criadorResumo = new com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioResumoDTO(
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

        return new InteresseResponseDTO(
                interesse.getId(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipoDeConta(),
                doacaoResumo,
                criadorResumo,
                interesse.getStatusInteresse(),
                interesse.getDataInteresse()
        );
    }
}
