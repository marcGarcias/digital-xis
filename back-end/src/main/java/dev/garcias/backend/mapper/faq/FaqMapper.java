package dev.garcias.backend.mapper.faq;

import dev.garcias.backend.dto.cms.CMSFaqDTO;
import dev.garcias.backend.dto.response.faq.FaqResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FaqMapper {

    @Mapping(source = "pergunta", target = "question")
    @Mapping(source = "resposta", target = "answer")
    FaqResponse toFaqResponse(CMSFaqDTO cmsFaqDTO);
}