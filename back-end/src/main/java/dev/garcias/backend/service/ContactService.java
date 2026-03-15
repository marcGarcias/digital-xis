package dev.garcias.backend.service;

import dev.garcias.backend.dto.request.ContactRequest;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private final EmailService emailService;
    private final CaptchaService captchaService;

    public ContactService(EmailService emailService, CaptchaService captchaService) {
        this.emailService = emailService;
        this.captchaService = captchaService;
    }

    public void process(ContactRequest request) {
        captchaService.validate(request.captchaToken());
        emailService.send(request);
    }
}