package dev.garcias.backend.dto.request;

import jakarta.validation.constraints.*;

public record ContactRequest(

        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 100)
        String name,

        @Size(max = 100)
        String company,

        Boolean noCompany,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 150)
        String email,

        @NotBlank(message = "Telefone é obrigatório")
        @Size(max = 30)
        String phone,

        @Size(max = 100)
        String instagram,

        Boolean noInstagram,

        @NotBlank(message = "Assunto é obrigatório")
        @Size(max = 150)
        String subject,

        @NotBlank(message = "Origem é obrigatória")
        @Size(max = 100)
        String source,

        @NotBlank(message = "Mensagem é obrigatória")
        @Size(max = 2000)
        String message,

        @AssertTrue(message = "É necessário aceitar a política de privacidade")
        Boolean privacyAccepted,

        @NotBlank(message = "Token de verificação é obrigatório")
        String captchaToken

) {
    public String resolvedCompany() {
        return Boolean.TRUE.equals(noCompany) ? "Não possui empresa" : company;
    }

    public String resolvedInstagram() {
        return Boolean.TRUE.equals(noInstagram) ? "Não possui Instagram" : instagram;
    }

    public String resolvedPrivacyStatus() {
        return Boolean.TRUE.equals(privacyAccepted) ? "Aceito" : "Não aceito";
    }
}