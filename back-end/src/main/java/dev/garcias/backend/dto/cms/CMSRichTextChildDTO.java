package dev.garcias.backend.dto.cms;

public record CMSRichTextChildDTO(
        String type,
        String text,
        Boolean bold,
        Boolean italic,
        Boolean underline,
        Boolean strikethrough,
        Boolean code
) {}