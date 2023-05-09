package com.dumas.sso.oauth.gateway.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/09 23:53
 */
public class IPFilter implements WebFilter {

    private final Set<String> whiteList;

    public IPFilter(String... whiteList) {
        this.whiteList = new HashSet<>(Arrays.asList(whiteList));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        if (whiteList.contains(ipAddress)) {
            return chain.filter(exchange);
        }
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
