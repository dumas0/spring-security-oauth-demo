package com.dumas.sso.oauth.gateway.config;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/10 00:42
 */
public class EmptyWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.empty();
    }
}
