package dev.garcias.backend.service.cms;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CMSCacheService {

    private static final Duration TTL = Duration.ofMinutes(10);

    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> Mono<T> get(String key, Supplier<Mono<T>> source, Class<T> type) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> Mono.fromCallable(() -> objectMapper.readValue(json, type)))
                .switchIfEmpty(
                        Mono.defer(source::get).flatMap(value ->
                                Mono.fromCallable(() -> objectMapper.writeValueAsString(value))
                                        .flatMap(json -> redisTemplate.opsForValue().set(key, json, TTL))
                                        .thenReturn(value)
                        )
                );
    }

    public <T> Mono<List<T>> getList(String key, Supplier<Mono<List<T>>> source, TypeReference<List<T>> type) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(json -> Mono.fromCallable(() -> objectMapper.readValue(json, type)))
                .switchIfEmpty(
                        Mono.defer(source::get).flatMap(value ->
                                Mono.fromCallable(() -> objectMapper.writeValueAsString(value))
                                        .flatMap(json -> redisTemplate.opsForValue().set(key, json, TTL))
                                        .thenReturn(value)
                        )
                );
    }
}