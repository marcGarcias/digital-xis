package dev.garcias.backend.controller;

import dev.garcias.backend.dto.response.about.AboutResponse;
import dev.garcias.backend.dto.response.carousel.CarouselResponse;
import dev.garcias.backend.dto.response.faq.FaqResponse;
import dev.garcias.backend.dto.response.info.InfoResponse;
import dev.garcias.backend.dto.response.review.ReviewResponse;
import dev.garcias.backend.exception.cms.CmsErrorHandler;
import dev.garcias.backend.service.CMSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CMSController {

    private final CMSService cmsService;

    public CMSController(CMSService cmsService) {
        this.cmsService = cmsService;
    }

    @GetMapping("/content/carousel")
    public Mono<ResponseEntity<CarouselResponse>> carouselContent() {
        return CmsErrorHandler.handle(
                cmsService.getCarouselContent().map(ResponseEntity::ok),
                "carousel"
        );
    }

    @GetMapping("/content/about")
    public Mono<ResponseEntity<AboutResponse>> aboutContent() {
        return CmsErrorHandler.handle(
                cmsService.getAboutContent().map(ResponseEntity::ok),
                "about"
        );
    }

    @GetMapping("/content/review")
    public Mono<ResponseEntity<List<ReviewResponse>>> reviewContent() {
        return CmsErrorHandler.handle(
                cmsService.getReviewContent().map(ResponseEntity::ok),
                "review"
        );
    }

    @GetMapping("/content/faq")
    public Mono<ResponseEntity<List<FaqResponse>>> faqContent() {
        return CmsErrorHandler.handle(
                cmsService.getFaqContent().map(ResponseEntity::ok),
                "faq"
        );
    }

    @GetMapping("/content/info")
    public Mono<ResponseEntity<InfoResponse>> infoContent() {
        return CmsErrorHandler.handle(
                cmsService.getInfoContent().map(ResponseEntity::ok),
                "info"
        );
    }
}