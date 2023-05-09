package com.dumas.sso.oauth.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/09 23:55
 */
public class RateLimitFilter implements WebFilter {

    private final Map<String, Integer> requestCount = new ConcurrentHashMap<>();
    private final int maxRequestsPerSecond;

    public RateLimitFilter(int maxRequestsPerSecond) {
        this.maxRequestsPerSecond = maxRequestsPerSecond;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        int count = requestCount.compute(path, (key, value) -> {
            if (value == null) {
                return 1;
            } else {
                return value + 1;
            }
        });
        if (count > maxRequestsPerSecond) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
