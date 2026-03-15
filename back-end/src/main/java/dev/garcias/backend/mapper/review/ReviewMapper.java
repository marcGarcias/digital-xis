package dev.garcias.backend.mapper.review;

import dev.garcias.backend.dto.cms.CMSReviewDTO;
import dev.garcias.backend.dto.response.review.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "nome", target = "name")
    @Mapping(source = "comentario", target = "comment")
    ReviewResponse toReviewResponse(CMSReviewDTO cmsReviewDTO);
}