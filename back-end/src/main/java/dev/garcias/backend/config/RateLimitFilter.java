package dev.garcias.backend.config;

import io.github.bucket4j.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {

    private final Map<String, Bucket> contactBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> contentBuckets = new ConcurrentHashMap<>();

    private Bucket createContactBucket() {
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket createContentBucket() {
        Bandwidth limit = Bandwidth.classic(60, Refill.greedy(60, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket resolveContactBucket(String ip) {
        return contactBuckets.computeIfAbsent(ip, k -> createContactBucket());
    }

    private Bucket resolveContentBucket(String ip) {
        return contentBuckets.computeIfAbsent(ip, k -> createContentBucket());
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();
        String ip = httpRequest.getRemoteAddr();

        if (path.equals("/api/contact") && httpRequest.getMethod().equals("POST")) {
            Bucket bucket = resolveContactBucket(ip);
            if (!bucket.tryConsume(1)) {
                sendRateLimitError(httpResponse);
                return;
            }
        } else if (path.startsWith("/api/content/") || path.startsWith("/uploads/")) {
            Bucket bucket = resolveContentBucket(ip);
            if (!bucket.tryConsume(1)) {
                sendRateLimitError(httpResponse);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void sendRateLimitError(HttpServletResponse httpResponse) throws IOException {
        httpResponse.setStatus(429);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("""
                {"success": false, "message": "Muitas requisicoes. Tente novamente mais tarde."}
                """);
    }
}