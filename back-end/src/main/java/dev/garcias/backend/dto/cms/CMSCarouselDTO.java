package dev.garcias.backend.dto.cms;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CMSCarouselDTO(
        @JsonProperty("Imagens_Desktop") List<CMSImageDTO> imagensDesktop,
        @JsonProperty("Imagens_Mobile") List<CMSImageDTO> imagensMobile
) {}