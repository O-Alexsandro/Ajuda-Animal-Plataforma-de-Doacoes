package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.doacao.Doacao;
import com.ajudaanimal.doacoes.entity.interesse.Interesse;
import com.ajudaanimal.doacoes.entity.interesse.InteresseDTO;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioDTO;
import com.ajudaanimal.doacoes.repository.doacao.DoacaoRepository;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@Slf4j
public class EmailSenderServiceImpl {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    public void enviarEmailIscricao(UsuarioDTO usuarioDTO) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setSubject("Confirmação de Inscrição - Ajuda Animal");
            helper.setTo(usuarioDTO.email());

            String template = obterTemplateEmailInscricao();
            template = template.replace("{{nome}}", usuarioDTO.nome());
            template = template.replace("{{nomeApp}}", "Ajuda Animal");
            template = template.replace("{{ano}}", String.valueOf(java.time.Year.now().getValue()));

            helper.setText(template, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            log.info("Erro ao enviar email de inscrição: {}", e.getMessage());
        }
    }

    public void enviarEmailInteresse(InteresseDTO interesseDTO){
        Usuario usuario = usuarioRepository.findById(interesseDTO.usuarioId()).orElseThrow(
                ()-> new EntityNotFoundException("Usuário não localizado"));

        Doacao doacao = doacaoRepository.findById(interesseDTO.doacaoId()).orElseThrow(
                ()-> new EntityNotFoundException("Doação não localizada"));

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setSubject("Alguém se interessou pelo seu item - Ajuda Animal");
            helper.setTo(usuario.getEmail());

            String template = obterTemplateInteresse();
            template = template.replace("{{nomeDono}}", doacao.getUsuario().getNome());
            template = template.replace("{{nomeInteressado}}", usuario.getNome());
            template = template.replace("{{nomeItem}}", doacao.getTitulo());
            template = template.replace("{{descricaoItem}}", doacao.getDescricao());
            template = template.replace("{{cidadeItem}}", doacao.getCidade());
            template = template.replace("{{emailInteressado}}", usuario.getEmail());
            template = template.replace("{{telefoneInteressado}}", usuario.getTelefone());

            template = template.replace("{{nomeApp}}", "Ajuda Animal");
            template = template.replace("{{ano}}", String.valueOf(java.time.Year.now().getValue()));

            helper.setText(template, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            log.info("Erro ao enviar email de interesse: {}", e.getMessage());
        }
    }

    public void enviarEmailCancelamentoItem(Doacao doacao, Interesse interesses){

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setSubject("Alguém se interessou pelo seu item - Ajuda Animal");
            helper.setTo(interesses.getUsuario().getEmail());


            String template = obterTemplateCancelamento();
            template = template.replace("{{nomeInteressado}}", interesses.getUsuario().getNome());
            template = template.replace("{{nomeDono}}", doacao.getUsuario().getNome());
            template = template.replace("{{nomeItem}}", doacao.getTitulo());
            template = template.replace("{{descricaoItem}}", doacao.getDescricao());
            template = template.replace("{{cidadeItem}}", doacao.getCidade());

            template = template.replace("{{nomeApp}}", "Ajuda Animal");
            template = template.replace("{{ano}}", String.valueOf(java.time.Year.now().getValue()));

            helper.setText(template, true);
            javaMailSender.send(message);

        } catch (Exception e){
            log.info("Erro ao enviar email de cancelamento: {}", e.getMessage());
        }
    }

    // Nova sobrecarga que envia email de cancelamento para cada interesse da lista
    public void enviarEmailCancelamentoItem(Doacao doacao, List<Interesse> interesses){
        if (interesses == null || interesses.isEmpty()) return;
        for (Interesse interesse : interesses) {
            try {
                // reutiliza o método existente que envia para um único interesse
                enviarEmailCancelamentoItem(doacao, interesse);
            } catch (Exception e) {
                log.info("Erro ao enviar email de cancelamento para um interesse: {}", e.getMessage());
            }
        }
    }

    public String obterTemplateEmailInscricao () throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emails/template-inscricao.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String obterTemplateInteresse() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emails/template-interesse.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    public String obterTemplateCancelamento() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emails/template-cancelamento.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}