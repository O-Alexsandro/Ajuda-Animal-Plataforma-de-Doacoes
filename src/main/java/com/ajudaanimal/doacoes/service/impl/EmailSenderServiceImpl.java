package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.usuario_ong.UsuarioDTO;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class EmailSenderServiceImpl {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

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

    public String obterTemplateEmailInscricao () throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emails/template-inscricao.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }
}