package dev.garcias.backend.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@Service
public class CaptchaService {

    private static final String VERIFY_URL =
            "https://challenges.cloudflare.com/turnstile/v0/siteverify";

    private final WebClient webClient;
    private final String secretKey;

    public CaptchaService(
            WebClient.Builder webClientBuilder,
            @Value("${cloudflare.turnstile.secret-key}") String secretKey
    ) {
        this.webClient = webClientBuilder.build();
        this.secretKey = secretKey;
    }

    public void validate(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Captcha token not found");
        }

        CaptchaResponse response = callTurnstile(token);

        if (response == null || !Boolean.TRUE.equals(response.success())) {
            log.warn("Invalid Turnstile. Errors: {}",
                    response != null ? response.errorCodes() : "null");
            throw new IllegalArgumentException("Invalid or expired security verification");
        }
    }

    private CaptchaResponse callTurnstile(String token) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", secretKey);
        form.add("response", token);

        try {
            return webClient.post()
                    .uri(VERIFY_URL)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue(form)
                    .retrieve()
                    .bodyToMono(CaptchaResponse.class)
                    .block();
        } catch (Exception ex) {
            log.error("Error communicating with Cloudflare Turnstile", ex);
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Failed to validate security verification.");
        }
    }

    private record CaptchaResponse(
            boolean success,
            @JsonProperty("error-codes") List<String> errorCodes
    ) {}
}