package dev.garcias.backend.dto.cms;

import java.util.List;

public record CMSListResponseWrapper<T>(
        List<T> data
) {}