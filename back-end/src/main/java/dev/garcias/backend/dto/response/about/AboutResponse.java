package dev.garcias.backend.dto.response.about;

import dev.garcias.backend.dto.response.shared.ImageDTO;

public record AboutResponse(
        String description,
        ImageDTO topLeftImageDesktop,
        ImageDTO bottomLeftImageDesktop,
        ImageDTO rightImageDesktop,
        ImageDTO bottomImageDesktop,
        ImageDTO topLeftImageMobile,
        ImageDTO bottomLeftImageMobile,
        ImageDTO rightImageMobile,
        ImageDTO bottomImageMobile
) {}