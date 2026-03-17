package dev.garcias.backend.service;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.List;

@Service
public class CMSService {

    private static final String CAROUSEL_PATH = "/api/carrocel?populate=*";
    private static final String ABOUT_PATH    = "/api/sobre?populate=*";
    private static final String REVIEW_PATH   = "/api/depoimentos?populate=*";
    private static final String FAQ_PATH      = "/api/faqs?populate=*";
    private static final String INFO_PATH     = "/api/info?populate=*";

    private static final Duration TTL = Duration.ofMinutes(10);

    private final WebClient webClient;
    private final CarouselMapper carouselMapper;
    private final AboutMapper aboutMapper;
    private final ReviewMapper reviewMapper;
    private final FaqMapper faqMapper;
    private final DefaultInfoMapper defaultInfoMapper;
    @Qualifier("reactiveStringRedisTemplate")
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public CMSService(
            WebClient.Builder webClientBuilder,
            @Value("${cms.base-url}") String cmsBaseUrl,
            CarouselMapper carouselMapper,
            AboutMapper aboutMapper,
            ReviewMapper reviewMapper,
            FaqMapper faqMapper,
            DefaultInfoMapper defaultInfoMapper, ReactiveRedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.webClient = webClientBuilder.baseUrl(cmsBaseUrl).build();
        this.carouselMapper = carouselMapper;
        this.aboutMapper = aboutMapper;
        this.reviewMapper = reviewMapper;
        this.faqMapper = faqMapper;
        this.defaultInfoMapper = defaultInfoMapper;
    }

    private <T> Mono<T> cachedGet(String key, Mono<T> source, Class<T> type) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> Mono.fromCallable(() -> objectMapper.readValue(json, type)))
                .switchIfEmpty(
                        source.flatMap(value ->
                                Mono.fromCallable(() -> objectMapper.writeValueAsString(value))
                                        .flatMap(json -> redisTemplate.opsForValue().set(key, json, TTL))
                                        .thenReturn(value)
                        )
                );
    }

    private <T> Mono<List<T>> cachedGetList(String key, Mono<List<T>> source, TypeReference<List<T>> type) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> Mono.fromCallable(() -> objectMapper.readValue(json, type)))
                .switchIfEmpty(
                        source.flatMap(value ->
                                Mono.fromCallable(() -> objectMapper.writeValueAsString(value))
                                        .flatMap(json -> redisTemplate.opsForValue().set(key, json, TTL))
                                        .thenReturn(value)
                        )
                );
    }

    public Mono<CarouselResponse> getCarouselContent() {
        Mono<CarouselResponse> source = webClient.get()
                .uri(CAROUSEL_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSSingleResponseWrapper<CMSCarouselDTO>>() {})
                .map(wrapper -> carouselMapper.toCarouselResponse(wrapper.data()));

        return cachedGet("cms:carousel", source, CarouselResponse.class);
    }


    public Mono<AboutResponse> getAboutContent() {
        Mono<AboutResponse> source = webClient.get()
                .uri(ABOUT_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSSingleResponseWrapper<CMSAboutDTO>>() {})
                .map(wrapper -> aboutMapper.toAboutResponse(wrapper.data()));

        return cachedGet("cms:about", source, AboutResponse.class);
    }

    public Mono<List<ReviewResponse>> getReviewContent() {
        Mono<List<ReviewResponse>> source = webClient.get()
                .uri(REVIEW_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSListResponseWrapper<CMSReviewDTO>>() {})
                .map(wrapper -> wrapper.data().stream().map(reviewMapper::toReviewResponse).toList());

        return cachedGetList("cms:reviews", source, new TypeReference<List<ReviewResponse>>() {});
    }


    public Mono<List<FaqResponse>> getFaqContent() {
        Mono<List<FaqResponse>> source = webClient.get()
                .uri(FAQ_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSListResponseWrapper<CMSFaqDTO>>() {})
                .map(wrapper -> wrapper.data().stream().map(faqMapper::toFaqResponse).toList());

        return cachedGetList("cms:faqs", source, new TypeReference<List<FaqResponse>>() {});
    }

    public Mono<InfoResponse> getInfoContent() {
        Mono<InfoResponse> source = webClient.get()
                .uri(INFO_PATH)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CMSSingleResponseWrapper<CMSInfoDTO>>() {})
                .map(wrapper -> defaultInfoMapper.toInfoResponse(wrapper.data()));

        return cachedGet("cms:info", source, InfoResponse.class);
    }
}