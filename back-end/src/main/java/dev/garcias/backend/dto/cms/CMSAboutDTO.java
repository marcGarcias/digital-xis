package dev.garcias.backend.dto.cms;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CMSAboutDTO(
        @JsonProperty("Descricao") List<CMSRichTextDTO> descricao,
        @JsonProperty("imagem_superior_esquerda_Desktop") CMSImageDTO imagemSuperiorEsquerdaDesktop,
        @JsonProperty("imagem_inferior_esquerda_Desktop") CMSImageDTO imagemInferiorEsquerdaDesktop,
        @JsonProperty("imagem_direita_Desktop") CMSImageDTO imagemDireitaDesktop,
        @JsonProperty("imagem_inferior_Desktop") CMSImageDTO imagemInferiorDesktop,
        @JsonProperty("imagem_superior_esquerda_Mobile") CMSImageDTO imagemSuperiorEsquerdaMobile,
        @JsonProperty("imagem_inferior_esquerda_Mobile") CMSImageDTO imagemInferiorEsquerdaMobile,
        @JsonProperty("imagem_direita_Mobile") CMSImageDTO imagemDireitaMobile,
        @JsonProperty("imagem_inferior_Mobile") CMSImageDTO imagemInferiorMobile
) {}