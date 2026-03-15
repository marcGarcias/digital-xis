package dev.garcias.backend.dto.response.info;

public record InfoResponse(
        String email,
        String cnpj,
        String copyrightYear,
        String whatsappRaw,
        String whatsappFormatted,
        String instagramFormatted,
        String instagramRaw
) {}