package dev.garcias.backend.service;

import dev.garcias.backend.dto.cms.*;
import dev.garcias.backend.dto.response.about.AboutResponse;
import dev.garcias.backend.dto.response.carousel.CarouselResponse;
import dev.garcias.backend.dto.response.faq.FaqResponse;
import dev.garcias.backend.dto.response.info.InfoResponse;
import dev.garcias.backend.dto.response.review.ReviewResponse;
import dev.garcias.backend.mapper.FaqMapper;
import dev.garcias.backend.mapper.info.DefaultInfoMapper;
import dev.garcias.backend.mapper.about.AboutMapper;
import dev.garcias.backend.mapper.carousel.CarouselMapper;
import dev.garcias.backend.mapper.review.ReviewMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CMSService {

    private static final String CAROUSEL_PATH = "/api/carrocel?populate=*";
    private static final String ABOUT_PATH    = "/api/sobre?populate=*";
    private static final String REVIEW_PATH   = "/api/depoimentos?populate=*";
    private static final String FAQ_PATH      = "/api/faqs?populate=*";
    private static final String INFO_PATH     = "/api/info?populate=*";

    private final WebClient webClient;
    private final CarouselMapper carouselMapper;
    private final AboutMapper aboutMapper;
    private final ReviewMapper reviewMapper;
    private final FaqMapper faqMapper;
    private final DefaultInfoMapper defaultInfoMapper;

    public CMSService(
            WebClient.Builder webClientBuilder,
            @Value("${cms.base-url}") String cmsBaseUrl,
            CarouselMapper carouselMapper,
            AboutMapper aboutMapper,
            ReviewMapper reviewMapper,
            FaqMapper faqMapper,
            DefaultInfoMapper defaultInfoMapper
    ) {
        this.webClient = webClientBuilder.baseUrl(cmsBaseUrl).build();
        this.carouselMapper = carouselMapper;
        this.aboutMapper = aboutMapper;
        this.reviewMapper = reviewMapper;
        this.faqMapper = faqMapper;
        this.defaultInfoMapper = defaultInfoMapper;
    }

    public Mono<CarouselResponse> getCarouselContent() {
        return webClient.get()
                .uri(CAROUSEL_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSSingleResponseWrapper<CMSCarouselDTO>>() {})
                .map(wrapper -> carouselMapper.toCarouselResponse(wrapper.data()));
    }

    public Mono<AboutResponse> getAboutContent() {
        return webClient.get()
                .uri(ABOUT_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSSingleResponseWrapper<CMSAboutDTO>>() {})
                .map(wrapper -> aboutMapper.toAboutResponse(wrapper.data()));
    }

    public Mono<List<ReviewResponse>> getReviewContent() {
        return webClient.get()
                .uri(REVIEW_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSListResponseWrapper<CMSReviewDTO>>() {})
                .map(wrapper -> wrapper.data().stream().map(reviewMapper::toReviewResponse).toList());
    }

    public Mono<List<FaqResponse>> getFaqContent() {
        return webClient.get()
                .uri(FAQ_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSListResponseWrapper<CMSFaqDTO>>() {})
                .map(wrapper -> wrapper.data().stream().map(faqMapper::toFaqResponse).toList());
    }

    public Mono<InfoResponse> getInfoContent() {
        return webClient.get()
                .uri(INFO_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSSingleResponseWrapper<CMSInfoDTO>>() {})
                .map(wrapper -> defaultInfoMapper.toInfoResponse(wrapper.data()));
    }
}