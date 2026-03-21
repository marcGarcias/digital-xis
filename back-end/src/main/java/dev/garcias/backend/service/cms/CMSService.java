package dev.garcias.backend.service.cms;

import lombok.RequiredArgsConstructor;
import tools.jackson.core.type.TypeReference;
import dev.garcias.backend.dto.cms.*;
import dev.garcias.backend.dto.response.about.AboutResponse;
import dev.garcias.backend.dto.response.carousel.CarouselResponse;
import dev.garcias.backend.dto.response.faq.FaqResponse;
import dev.garcias.backend.dto.response.info.InfoResponse;
import dev.garcias.backend.dto.response.review.ReviewResponse;
import dev.garcias.backend.mapper.faq.FaqMapper;
import dev.garcias.backend.mapper.info.DefaultInfoMapper;
import dev.garcias.backend.mapper.about.AboutMapper;
import dev.garcias.backend.mapper.carousel.CarouselMapper;
import dev.garcias.backend.mapper.review.ReviewMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CMSService {

    private final StrapiClient cmsClient;
    private final CMSCacheService cacheService;

    private final CarouselMapper carouselMapper;
    private final AboutMapper aboutMapper;
    private final ReviewMapper reviewMapper;
    private final FaqMapper faqMapper;
    private final DefaultInfoMapper defaultInfoMapper;

    public Mono<CarouselResponse> getCarouselContent() {
        return cacheService.get("cms:carousel",
                () -> cmsClient.getCarousel()
                        .map(wrapper -> carouselMapper.toCarouselResponse(wrapper.data())),
                CarouselResponse.class);
    }

    public Mono<AboutResponse> getAboutContent() {
        return cacheService.get("cms:about",
                () -> cmsClient.getAbout()
                        .map(wrapper -> aboutMapper.toAboutResponse(wrapper.data())),
                AboutResponse.class);
    }

    public Mono<List<ReviewResponse>> getReviewContent() {
        return cacheService.getList("cms:reviews",
                () -> cmsClient.getReviews()
                        .map(wrapper -> wrapper.data().stream()
                                .map(reviewMapper::toReviewResponse)
                                .toList()),
                new TypeReference<List<ReviewResponse>>() {});
    }

    public Mono<List<FaqResponse>> getFaqContent() {
        return cacheService.getList("cms:faqs",
                () -> cmsClient.getFaq()
                        .map(wrapper -> wrapper.data().stream()
                                .map(faqMapper::toFaqResponse)
                                .toList()),
                new TypeReference<List<FaqResponse>>() {});
    }

    public Mono<InfoResponse> getInfoContent() {
        return cacheService.get("cms:info",
                () -> cmsClient.getInfo()
                        .map(wrapper -> defaultInfoMapper.toInfoResponse(wrapper.data())),
                InfoResponse.class);
    }
}