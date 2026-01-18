package com.ajudaanimal.doacoes.service;

import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.InteresseDTO;
import com.ajudaanimal.doacoes.entity.interesse.InteresseResponseDTO;

import java.util.List;

public interface InteresseService {

    public Interesse criarInteresse(InteresseDTO interesseDTO);
    public List<Interesse> listarInteresse();
    public Interesse buscarInteressePorId(Long id);
    public List<InteresseResponseDTO> buscarInteressePorUsuario(Long idUsuario);
    public Interesse cancelarInteresse(Long idUsuario, Long idInteresse);
    public Interesse recusarInteresse(Long idUsuario, Long idInteresse, Long idDoacao);
    public InteresseResponseDTO buscarInteressePorStatus(String statusInteresse);
    public List<InteresseResponseDTO> listarInteressePorDoacao(Long idDoacao);

    // Novos métodos que retornam DTOs para uso em controllers
    public List<InteresseResponseDTO> listarInteresseDTO();
    public InteresseResponseDTO buscarInteressePorIdDTO(Long id);
}
