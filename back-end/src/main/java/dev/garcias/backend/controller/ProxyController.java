package dev.garcias.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/uploads")
public class ProxyController {

    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

    private final WebClient webClient;

    public ProxyController(
            WebClient.Builder webClientBuilder,
            @Value("${cms.base-url}") String cmsBaseUrl
    ) {
        this.webClient = webClientBuilder
                .baseUrl(cmsBaseUrl)
                .build();
    }

    @GetMapping("/**")
    public void proxyUploads(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getRequestURI();

        // Segurança contra Path Traversal e SSRF
        if (path.contains("..") || path.contains("%2e") || path.contains("%2E") || !path.startsWith("/uploads/")) {
            log.warn("Tentativa de acesso invalido ou path traversal interceptada no proxy: {}", path);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path requested.");
            return;
        }

        try {
            byte[] bytes = webClient.get()
                    .uri(path)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();

            if (bytes == null) {
                log.warn("Resource not found in CMS: {}", path);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            String contentType = request.getServletContext().getMimeType(path);
            response.setContentType(contentType != null ? contentType : DEFAULT_CONTENT_TYPE);
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);

        } catch (WebClientResponseException ex) {
            log.error("Error retrieving resource in CMS: path={}, status={}", path, ex.getStatusCode());
            response.sendError(ex.getStatusCode().value(), "Error retrieving resource in CMS.");
        } catch (Exception ex) {
            log.error("Unexpected error in upload proxy: path={}", path, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error.");
        }
    }
}