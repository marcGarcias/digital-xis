package dev.garcias.backend.dto.cms;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CMSReviewDTO(
        @JsonProperty("Nome") String nome,
        @JsonProperty("Comentario") String comentario
) {}