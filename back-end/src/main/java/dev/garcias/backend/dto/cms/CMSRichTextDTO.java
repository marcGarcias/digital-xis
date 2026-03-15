package dev.garcias.backend.dto.cms;

import java.util.List;

public record CMSRichTextDTO(
        String type,
        Integer level,
        List<CMSRichTextChildDTO> children
) {}