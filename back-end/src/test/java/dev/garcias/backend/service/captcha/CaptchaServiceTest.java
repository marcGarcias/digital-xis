package dev.garcias.backend.service.captcha;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaptchaServiceTest {

    @Mock
    private TurnstileClient turnstileClient;

    @InjectMocks
    private CaptchaService captchaService;

    @Test
    @DisplayName("1: Should throw exception when token is null")
    void shouldThrowWhenTokenIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> captchaService.validate(null));
    }

    @Test
    @DisplayName("2: Should call TokenSender when token is valid")
    void shouldCallTokenSenderWhenTokenIsValid() {
        String token = "valid-token";

        when(turnstileClient.send(token))
                .thenReturn(new TurnstileClient.CaptchaResponse(true, null));

        captchaService.validate(token);

        verify(turnstileClient).send(token);
    }

    @Test
    @DisplayName("3: Should throw exception when captcha is invalid")
    void shouldThrowWhenCaptchaIsInvalid() {
        String token = "valid-token";

        when(turnstileClient.send(token))
                .thenReturn(new TurnstileClient.CaptchaResponse(false, List.of("invalid")));

        assertThrows(IllegalArgumentException.class,
                () -> captchaService.validate(token));
    }

    @Test
    @DisplayName("4: Should throw exception when response is null")
    void shouldThrowWhenResponseIsNull() {
        String token = "valid-token";

        when(turnstileClient.send(token))
                .thenReturn(null);

        assertThrows(IllegalArgumentException.class,
                () -> captchaService.validate(token));
    }
}