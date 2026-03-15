package dev.garcias.backend.mapper.carousel;

import dev.garcias.backend.dto.cms.CMSImageDTO;
import dev.garcias.backend.dto.cms.CMSCarouselDTO;
import dev.garcias.backend.dto.response.carousel.CarouselResponse;
import dev.garcias.backend.mapper.shared.ImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImageMapper.class}) // ← adiciona uses
public interface CarouselMapper {

    @Mapping(source = "imagensDesktop", target = "desktopImages")
    @Mapping(source = "imagensMobile", target = "mobileImages")
    CarouselResponse toCarouselResponse(CMSCarouselDTO carouselDTO);
}