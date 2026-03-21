package dev.garcias.backend.service.cms;

import dev.garcias.backend.dto.cms.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
public class StrapiClient {

    private final WebClient webClient;

    public StrapiClient(WebClient.Builder builder,
                     @Value("${cms.base-url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    public StrapiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<CMSSingleResponseWrapper<CMSCarouselDTO>> getCarousel() {
        return webClient.get()
                .uri("/api/carrocel?populate=*")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<CMSSingleResponseWrapper<CMSAboutDTO>> getAbout() {
        return webClient.get()
                .uri("/api/sobre?populate=*")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<CMSListResponseWrapper<CMSReviewDTO>> getReviews() {
        return webClient.get()
                .uri("/api/depoimentos?populate=*")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<CMSListResponseWrapper<CMSFaqDTO>> getFaq() {
        return webClient.get()
                .uri("/api/faqs?populate=*")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }

    public Mono<CMSSingleResponseWrapper<CMSInfoDTO>> getInfo() {
        return webClient.get()
                .uri("/api/info?populate=*")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}