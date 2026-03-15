package dev.garcias.backend.dto.cms;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CMSFaqDTO(
        @JsonProperty("Pergunta") String pergunta,
        @JsonProperty("Resposta") String resposta
) {}