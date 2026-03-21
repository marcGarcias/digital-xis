package dev.garcias.backend.service.captcha;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CaptchaService {

    private final TurnstileClient turnstileClient;

    public void validate(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Captcha token not found");
        }

        TurnstileClient.CaptchaResponse response = turnstileClient.send(token);

        if (response == null || !response.success()) {
            log.warn("Invalid Turnstile. Errors: {}",
                    response != null ? response.errorCodes() : "null");

            throw new IllegalArgumentException("Invalid or expired security verification");
        }
    }
}