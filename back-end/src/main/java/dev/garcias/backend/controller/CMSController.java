package dev.garcias.backend.controller;

import dev.garcias.backend.dto.response.about.AboutResponse;
import dev.garcias.backend.dto.response.carousel.CarouselResponse;
import dev.garcias.backend.dto.response.faq.FaqResponse;
import dev.garcias.backend.dto.response.info.InfoResponse;
import dev.garcias.backend.dto.response.review.ReviewResponse;
import dev.garcias.backend.exception.ApiError;
import dev.garcias.backend.exception.cms.CmsErrorHandler;
import dev.garcias.backend.service.cms.CMSService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "CMS Content", description = "Provides endpoints for retrieving dynamic content stored in the CMS, including carousel, about page, reviews, FAQ, and general info.")
public class CMSController {

    private final CMSService cmsService;

    public CMSController(CMSService cmsService) {
        this.cmsService = cmsService;
    }

    @GetMapping("/content/carousel")
    @Operation(summary = "Get carousel content", description = "Retrieves the collection of items for the home carousel.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content retrieved successfully."),
            @ApiResponse(responseCode = "502", description = "Error communicating with CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "504", description = "Timeout when communicating with the CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Mono<ResponseEntity<CarouselResponse>> carouselContent() {
        return CmsErrorHandler.handle(
                cmsService.getCarouselContent().map(ResponseEntity::ok),
                "carousel"
        );
    }

    @GetMapping("/content/about")
    @Operation(summary = "Get about page content", description = "Retrieves the text and images for the 'About Us' section.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content retrieved successfully."),
            @ApiResponse(responseCode = "502", description = "Error communicating with CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "504", description = "Timeout when communicating with the CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Mono<ResponseEntity<AboutResponse>> aboutContent() {
        return CmsErrorHandler.handle(
                cmsService.getAboutContent().map(ResponseEntity::ok),
                "about"
        );
    }

    @GetMapping("/content/review")
    @Operation(summary = "Get customer reviews", description = "Retrieves a list of testimonials and customer reviews.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content retrieved successfully."),
            @ApiResponse(responseCode = "502", description = "Error communicating with CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "504", description = "Timeout when communicating with the CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Mono<ResponseEntity<List<ReviewResponse>>> reviewContent() {
        return CmsErrorHandler.handle(
                cmsService.getReviewContent().map(ResponseEntity::ok),
                "review"
        );
    }

    @GetMapping("/content/faq")
    @Operation(summary = "Get FAQ list", description = "Retrieves a list of frequently asked questions and their answers.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content retrieved successfully."),
            @ApiResponse(responseCode = "502", description = "Error communicating with CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "504", description = "Timeout when communicating with the CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Mono<ResponseEntity<List<FaqResponse>>> faqContent() {
        return CmsErrorHandler.handle(
                cmsService.getFaqContent().map(ResponseEntity::ok),
                "faq"
        );
    }

    @GetMapping("/content/info")
    @Operation(summary = "Get general site info", description = "Retrieves general website information, such as contact details and business hours.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Content retrieved successfully."),
            @ApiResponse(responseCode = "502", description = "Error communicating with CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "504", description = "Timeout when communicating with the CMS.", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public Mono<ResponseEntity<InfoResponse>> infoContent() {
        return CmsErrorHandler.handle(
                cmsService.getInfoContent().map(ResponseEntity::ok),
                "info"
        );
    }
}