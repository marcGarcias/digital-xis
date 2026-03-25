package dev.garcias.backend.service.form;

import dev.garcias.backend.dto.request.ContactRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private final String destinationEmail = "test@example.com";

    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailCaptor;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, destinationEmail);
    }

    @Test
    @DisplayName("1 Should sanitize CRLF characters from subject to prevent header injection")
    void shouldSanitizeCRLFFromSubject() {
        // Arrange
        ContactRequest request = mock(ContactRequest.class);
        when(request.subject()).thenReturn("Inquiry\r\nBcc: attacker@email.com");

        // Act
        emailService.send(request);

        // Assert
        verify(mailSender).send(mailCaptor.capture());
        SimpleMailMessage sentMail = mailCaptor.getValue();
        
        assertNotNull(sentMail.getTo());
        assertEquals(destinationEmail, sentMail.getTo()[0]);
        // The subject is trimmed and CRLF removed
        assertEquals("Novo contato recebido: InquiryBcc: attacker@email.com", sentMail.getSubject());
    }

    @Test
    @DisplayName("2 Should truncate subject exceeding max length of 150 characters")
    void shouldTruncateSubjectExceedingMaxLength() {
        // Arrange
        ContactRequest request = mock(ContactRequest.class);
        String longSubject = "a".repeat(200);
        when(request.subject()).thenReturn(longSubject);

        // Act
        emailService.send(request);

        // Assert
        verify(mailSender).send(mailCaptor.capture());
        SimpleMailMessage sentMail = mailCaptor.getValue();
        
        String expectedSanitizedSubject = "a".repeat(150);
        assertEquals("Novo contato recebido: " + expectedSanitizedSubject, sentMail.getSubject());
    }
}
