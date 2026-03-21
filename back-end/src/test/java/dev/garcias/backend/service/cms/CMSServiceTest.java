package dev.garcias.backend.service.cms;

import dev.garcias.backend.dto.cms.*;
import dev.garcias.backend.dto.response.about.AboutResponse;
import dev.garcias.backend.dto.response.carousel.CarouselResponse;
import dev.garcias.backend.dto.response.faq.FaqResponse;
import dev.garcias.backend.dto.response.info.InfoResponse;
import dev.garcias.backend.dto.response.review.ReviewResponse;
import dev.garcias.backend.mapper.about.AboutMapper;
import dev.garcias.backend.mapper.carousel.CarouselMapper;
import dev.garcias.backend.mapper.faq.FaqMapper;
import dev.garcias.backend.mapper.info.DefaultInfoMapper;
import dev.garcias.backend.mapper.review.ReviewMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CMSServiceTest {

    @Mock
    private StrapiClient cmsClient;

    @Mock
    private CMSCacheService cacheService;

    @Mock
    private CarouselMapper carouselMapper;

    @Mock
    private AboutMapper aboutMapper;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private FaqMapper faqMapper;

    @Mock
    private DefaultInfoMapper defaultInfoMapper;

    @InjectMocks
    private CMSService cmsService;

    // ─── getCarouselContent ───────────────────────────────────────────────────

    @Test
    @DisplayName("1 Carousel: should return cached carousel response")
    void getCarouselContent_shouldReturnCachedResponse() {
        CarouselResponse expected = new CarouselResponse(List.of(), List.of());

        when(cacheService.get(eq("cms:carousel"), any(), eq(CarouselResponse.class)))
                .thenReturn(Mono.just(expected));

        CarouselResponse result = cmsService.getCarouselContent().block();

        assertEquals(expected, result);
        verify(cacheService).get(eq("cms:carousel"), any(), eq(CarouselResponse.class));
        verifyNoInteractions(cmsClient, carouselMapper);
    }

    @Test
    @DisplayName("2 Carousel: should map CMSCarouselDTO through carouselMapper")
    void getCarouselContent_shouldMapThroughCarouselMapper() {
        CMSCarouselDTO dto = new CMSCarouselDTO(List.of(), List.of());
        CMSSingleResponseWrapper<CMSCarouselDTO> wrapper = new CMSSingleResponseWrapper<>(dto);
        CarouselResponse expected = new CarouselResponse(List.of(), List.of());

        when(cmsClient.getCarousel()).thenReturn(Mono.just(wrapper));
        when(carouselMapper.toCarouselResponse(dto)).thenReturn(expected);
        when(cacheService.get(eq("cms:carousel"), any(), eq(CarouselResponse.class)))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<CarouselResponse>>>getArgument(1).get());

        CarouselResponse result = cmsService.getCarouselContent().block();

        assertEquals(expected, result);
        verify(cmsClient).getCarousel();
        verify(carouselMapper).toCarouselResponse(dto);
    }

    @Test
    @DisplayName("3 Carousel: should propagate error from StrapiClient")
    void getCarouselContent_shouldPropagateClientError() {
        RuntimeException error = new RuntimeException("CMS unavailable");

        when(cmsClient.getCarousel()).thenReturn(Mono.error(error));
        when(cacheService.get(eq("cms:carousel"), any(), eq(CarouselResponse.class)))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<CarouselResponse>>>getArgument(1).get());

        assertThrows(RuntimeException.class, () -> cmsService.getCarouselContent().block());
        verifyNoInteractions(carouselMapper);
    }

    // ─── getAboutContent ─────────────────────────────────────────────────────

    @Test
    @DisplayName("4 About: should return cached about response")
    void getAboutContent_shouldReturnCachedResponse() {
        AboutResponse expected = new AboutResponse(
                "desc", null, null, null, null, null, null, null, null
        );

        when(cacheService.get(eq("cms:about"), any(), eq(AboutResponse.class)))
                .thenReturn(Mono.just(expected));

        AboutResponse result = cmsService.getAboutContent().block();

        assertEquals(expected, result);
        verify(cacheService).get(eq("cms:about"), any(), eq(AboutResponse.class));
        verifyNoInteractions(cmsClient, aboutMapper);
    }

    @Test
    @DisplayName("5 About: should map CMSAboutDTO through aboutMapper")
    void getAboutContent_shouldMapThroughAboutMapper() {
        CMSAboutDTO dto = new CMSAboutDTO(
                List.of(), null, null, null, null, null, null, null, null
        );
        CMSSingleResponseWrapper<CMSAboutDTO> wrapper = new CMSSingleResponseWrapper<>(dto);
        AboutResponse expected = new AboutResponse(
                "desc", null, null, null, null, null, null, null, null
        );

        when(cmsClient.getAbout()).thenReturn(Mono.just(wrapper));
        when(aboutMapper.toAboutResponse(dto)).thenReturn(expected);
        when(cacheService.get(eq("cms:about"), any(), eq(AboutResponse.class)))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<AboutResponse>>>getArgument(1).get());

        AboutResponse result = cmsService.getAboutContent().block();

        assertEquals(expected, result);
        verify(cmsClient).getAbout();
        verify(aboutMapper).toAboutResponse(dto);
    }

    @Test
    @DisplayName("6 About: should propagate error from StrapiClient")
    void getAboutContent_shouldPropagateClientError() {
        RuntimeException error = new RuntimeException("CMS unavailable");

        when(cmsClient.getAbout()).thenReturn(Mono.error(error));
        when(cacheService.get(eq("cms:about"), any(), eq(AboutResponse.class)))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<AboutResponse>>>getArgument(1).get());

        assertThrows(RuntimeException.class, () -> cmsService.getAboutContent().block());
        verifyNoInteractions(aboutMapper);
    }

    // ─── getReviewContent ────────────────────────────────────────────────────

    @Test
    @DisplayName("7 Review: should return cached review list")
    void getReviewContent_shouldReturnCachedResponse() {
        List<ReviewResponse> expected = List.of(new ReviewResponse("Ana", "Ótimo!"));

        when(cacheService.getList(eq("cms:reviews"), any(), any()))
                .thenAnswer(inv -> Mono.just(expected));

        List<ReviewResponse> result = cmsService.getReviewContent().block();

        assertEquals(expected, result);
        verify(cacheService).getList(eq("cms:reviews"), any(), any());
        verifyNoInteractions(cmsClient, reviewMapper);
    }

    @Test
    @DisplayName("8 Review: should map each CMSReviewDTO through reviewMapper")
    void getReviewContent_shouldMapEachItemThroughReviewMapper() {
        CMSReviewDTO dto1 = new CMSReviewDTO("Ana", "Ótimo!");
        CMSReviewDTO dto2 = new CMSReviewDTO("João", "Recomendo!");
        CMSListResponseWrapper<CMSReviewDTO> wrapper = new CMSListResponseWrapper<>(List.of(dto1, dto2));
        ReviewResponse res1 = new ReviewResponse("Ana", "Ótimo!");
        ReviewResponse res2 = new ReviewResponse("João", "Recomendo!");

        when(cmsClient.getReviews()).thenReturn(Mono.just(wrapper));
        when(reviewMapper.toReviewResponse(dto1)).thenReturn(res1);
        when(reviewMapper.toReviewResponse(dto2)).thenReturn(res2);
        when(cacheService.getList(eq("cms:reviews"), any(), any()))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<List<ReviewResponse>>>>getArgument(1).get());

        List<ReviewResponse> result = cmsService.getReviewContent().block();

        assertEquals(List.of(res1, res2), result);
        verify(cmsClient).getReviews();
        verify(reviewMapper).toReviewResponse(dto1);
        verify(reviewMapper).toReviewResponse(dto2);
    }

    @Test
    @DisplayName("9 Review: should return empty list when CMS has no reviews")
    void getReviewContent_shouldReturnEmptyList() {
        CMSListResponseWrapper<CMSReviewDTO> wrapper = new CMSListResponseWrapper<>(List.of());

        when(cmsClient.getReviews()).thenReturn(Mono.just(wrapper));
        when(cacheService.getList(eq("cms:reviews"), any(), any()))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<List<ReviewResponse>>>>getArgument(1).get());

        List<ReviewResponse> result = cmsService.getReviewContent().block();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(reviewMapper);
    }

    // ─── getFaqContent ───────────────────────────────────────────────────────

    @Test
    @DisplayName("10 FAQ: should return cached faq list")
    void getFaqContent_shouldReturnCachedResponse() {
        List<FaqResponse> expected = List.of(new FaqResponse("Pergunta?", "Resposta."));

        when(cacheService.getList(eq("cms:faqs"), any(), any()))
                .thenAnswer(inv -> Mono.just(expected));

        List<FaqResponse> result = cmsService.getFaqContent().block();

        assertEquals(expected, result);
        verify(cacheService).getList(eq("cms:faqs"), any(), any());
        verifyNoInteractions(cmsClient, faqMapper);
    }

    @Test
    @DisplayName("11 FAQ: should map each CMSFaqDTO through faqMapper")
    void getFaqContent_shouldMapEachItemThroughFaqMapper() {
        CMSFaqDTO dto1 = new CMSFaqDTO("Pergunta 1?", "Resposta 1.");
        CMSFaqDTO dto2 = new CMSFaqDTO("Pergunta 2?", "Resposta 2.");
        CMSListResponseWrapper<CMSFaqDTO> wrapper = new CMSListResponseWrapper<>(List.of(dto1, dto2));
        FaqResponse res1 = new FaqResponse("Pergunta 1?", "Resposta 1.");
        FaqResponse res2 = new FaqResponse("Pergunta 2?", "Resposta 2.");

        when(cmsClient.getFaq()).thenReturn(Mono.just(wrapper));
        when(faqMapper.toFaqResponse(dto1)).thenReturn(res1);
        when(faqMapper.toFaqResponse(dto2)).thenReturn(res2);
        when(cacheService.getList(eq("cms:faqs"), any(), any()))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<List<FaqResponse>>>>getArgument(1).get());

        List<FaqResponse> result = cmsService.getFaqContent().block();

        assertEquals(List.of(res1, res2), result);
        verify(cmsClient).getFaq();
        verify(faqMapper).toFaqResponse(dto1);
        verify(faqMapper).toFaqResponse(dto2);
    }

    @Test
    @DisplayName("12 FAQ: should return empty list when CMS has no faqs")
    void getFaqContent_shouldReturnEmptyList() {
        CMSListResponseWrapper<CMSFaqDTO> wrapper = new CMSListResponseWrapper<>(List.of());

        when(cmsClient.getFaq()).thenReturn(Mono.just(wrapper));
        when(cacheService.getList(eq("cms:faqs"), any(), any()))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<List<FaqResponse>>>>getArgument(1).get());

        List<FaqResponse> result = cmsService.getFaqContent().block();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verifyNoInteractions(faqMapper);
    }

    // ─── getInfoContent ──────────────────────────────────────────────────────

    @Test
    @DisplayName("13 Info: should return cached info response")
    void getInfoContent_shouldReturnCachedResponse() {
        InfoResponse expected = new InfoResponse(
                "email@test.com", "00.000.000/0001-00", "2024",
                "5511999999999", "(55) 11 99999-9999",
                "@garcias", "garcias"
        );

        when(cacheService.get(eq("cms:info"), any(), eq(InfoResponse.class)))
                .thenReturn(Mono.just(expected));

        InfoResponse result = cmsService.getInfoContent().block();

        assertEquals(expected, result);
        verify(cacheService).get(eq("cms:info"), any(), eq(InfoResponse.class));
        verifyNoInteractions(cmsClient, defaultInfoMapper);
    }

    @Test
    @DisplayName("14 Info: should map CMSInfoDTO through defaultInfoMapper")
    void getInfoContent_shouldMapThroughDefaultInfoMapper() {
        CMSInfoDTO dto = new CMSInfoDTO(
                "email@test.com", "00.000.000/0001-00", "2024",
                "5511999999999", "garcias"
        );
        CMSSingleResponseWrapper<CMSInfoDTO> wrapper = new CMSSingleResponseWrapper<>(dto);
        InfoResponse expected = new InfoResponse(
                "email@test.com", "00.000.000/0001-00", "2024",
                "5511999999999", "(55) 11 99999-9999",
                "@garcias", "garcias"
        );

        when(cmsClient.getInfo()).thenReturn(Mono.just(wrapper));
        when(defaultInfoMapper.toInfoResponse(dto)).thenReturn(expected);
        when(cacheService.get(eq("cms:info"), any(), eq(InfoResponse.class)))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<InfoResponse>>>getArgument(1).get());

        InfoResponse result = cmsService.getInfoContent().block();

        assertEquals(expected, result);
        verify(cmsClient).getInfo();
        verify(defaultInfoMapper).toInfoResponse(dto);
    }

    @Test
    @DisplayName("15 Info: should propagate error from StrapiClient")
    void getInfoContent_shouldPropagateClientError() {
        RuntimeException error = new RuntimeException("CMS unavailable");

        when(cmsClient.getInfo()).thenReturn(Mono.error(error));
        when(cacheService.get(eq("cms:info"), any(), eq(InfoResponse.class)))
                .thenAnswer(inv -> inv.<java.util.function.Supplier<Mono<InfoResponse>>>getArgument(1).get());

        assertThrows(RuntimeException.class, () -> cmsService.getInfoContent().block());
        verifyNoInteractions(defaultInfoMapper);
    }
}