package dev.garcias.backend.service.form;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.garcias.backend.dto.request.ContactRequest;
import dev.garcias.backend.service.captcha.CaptchaService;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private CaptchaService captchaService;

    @InjectMocks
    private ContactService contactService;

    @Test
    @DisplayName("1 Contact: should validate captcha and send email")
    void shouldValidateCaptchaAndSendEmail() {
        ContactRequest request = mock(ContactRequest.class);

        when(request.captchaToken()).thenReturn("token");

        contactService.process(request);

        verify(captchaService).validate("token");
        verify(emailService).send(request);
    }
}