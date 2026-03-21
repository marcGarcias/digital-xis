package dev.garcias.backend.service.form;

import dev.garcias.backend.dto.request.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final int MAX_SUBJECT_LENGTH = 150;

    private final JavaMailSender mailSender;
    private final String destinationEmail;

    public EmailService(
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String destinationEmail
    ) {
        this.mailSender = mailSender;
        this.destinationEmail = destinationEmail;
    }

    public void send(ContactRequest request) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(destinationEmail);
        mail.setSubject("Novo contato recebido: " + sanitizeSubject(request.subject()));
        mail.setText(buildBody(request));
        mailSender.send(mail);
    }

    private String sanitizeSubject(String subject) {
        String sanitized = subject.replaceAll("[\\r\\n]", "").trim();
        return sanitized.length() > MAX_SUBJECT_LENGTH
                ? sanitized.substring(0, MAX_SUBJECT_LENGTH)
                : sanitized;
    }

    private String buildBody(ContactRequest request) {
        return "Novo lead recebido:\n\n" +
                "Nome: "              + request.name()                  + "\n" +
                "Empresa: "           + request.resolvedCompany()        + "\n" +
                "Email: "             + request.email()                  + "\n" +
                "Telefone: "          + request.phone()                  + "\n" +
                "Instagram: "         + request.resolvedInstagram()      + "\n" +
                "Como nos conheceu: " + request.source()                 + "\n\n" +
                "Status LGPD: "       + request.resolvedPrivacyStatus()  + "\n\n" +
                "Mensagem:\n"         + request.message();
    }
}