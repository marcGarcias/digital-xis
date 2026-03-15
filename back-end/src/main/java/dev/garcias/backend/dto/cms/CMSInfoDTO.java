package dev.garcias.backend.dto.cms;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CMSInfoDTO(
        @JsonProperty("Email") String email,
        @JsonProperty("CNPJ") String cnpj,
        @JsonProperty("Ano_do_Copyrights") String anoDoCopyrights,
        @JsonProperty("Whatsapp") String whatsapp,
        @JsonProperty("Instagram") String instagram
) {}