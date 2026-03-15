package dev.garcias.backend.dto.response.carousel;

import dev.garcias.backend.dto.response.shared.ImageDTO;

import java.util.List;

public record CarouselResponse(
        List<ImageDTO> desktopImages,
        List<ImageDTO> mobileImages
) {}