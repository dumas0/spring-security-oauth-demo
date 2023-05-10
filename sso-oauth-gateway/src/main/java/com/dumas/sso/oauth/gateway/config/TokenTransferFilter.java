package com.dumas.sso.oauth.gateway.config;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 将 token 信息传递到下游服务中
 *
 * @author dumas
 * @desc TODO
 * @date 2023/05/06 23:51
 */
@Slf4j
public class TokenTransferFilter implements WebFilter {

    private final List<String> skipPaths;

    public TokenTransferFilter(List<String> skipPaths) {
        this.skipPaths = skipPaths;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (skipPaths.stream()
                .map(skipPath -> skipPath.replaceAll("\\*", ".*"))
                .anyMatch(path::matches)) {
            return chain.filter(exchange);
        }
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .flatMap(authentication -> {
                    ServerHttpRequest request = exchange.getRequest();
                    request = request.mutate()
                            .header("tokenInfo", JSON.toJSONString(authentication.getPrincipal()))
                            .build();

                    ServerWebExchange newExchange = exchange.mutate().request(request).build();

                    return chain.filter(newExchange);
                });
    }
}
