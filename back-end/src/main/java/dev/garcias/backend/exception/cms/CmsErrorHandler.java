package dev.garcias.backend.exception.cms;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeoutException;

@Slf4j
public final class CmsErrorHandler {

    private CmsErrorHandler() {}

    public static <T> Mono<T> handle(Mono<T> mono, String context) {
        return mono
                .doOnError(WebClientResponseException.class, ex ->
                        log.error("CMS {} error: status={}", context, ex.getStatusCode()))
                .doOnError(TimeoutException.class, ex ->
                        log.error("Timeout CMS {}", context))
                .onErrorMap(WebClientResponseException.class, ex ->
                        new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error communicating with CMS", ex))
                .onErrorMap(TimeoutException.class, ex ->
                        new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Timeout when communicating with the CMS.", ex));
    }
}