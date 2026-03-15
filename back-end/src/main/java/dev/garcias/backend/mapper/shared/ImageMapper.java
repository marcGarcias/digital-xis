package dev.garcias.backend.mapper.shared;

import dev.garcias.backend.dto.cms.CMSImageDTO;
import dev.garcias.backend.dto.response.shared.ImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "url", expression = "java(\"/uploads\" + extractPath(cmsImageDTO.url()))")
    ImageDTO toImageDTO(CMSImageDTO cmsImageDTO);

    default String extractPath(String url) {
        if (url == null) return "";
        // Se já for só o path: /uploads/img.jpg → pega só /img.jpg
        int idx = url.indexOf("/uploads/");
        if (idx == -1) return url;
        return url.substring(idx + "/uploads".length()); // → /img.jpg
    }
}