package com.dumas.sso.oauth.gateway.config;

import com.dumas.sso.oauth.gateway.service.IpWhiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/06 23:53
 */
@Configuration
@EnableWebFluxSecurity
public class ResourceServerConfig {
    @Autowired
    private CustomReactiveAuthorizationManager customReactiveAuthorizationManager;

    private static final List<String> skipPaths = Arrays.asList("/oauth2/**",
            "/doc.html/**", "/webjars/**", "/swagger-ui.html",
            "/v2/**", "/swagger-resources", "swagger-resources/**", "/favicon.ico");

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {

        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                .jwtDecoder(jwtDecoder())
                .and()
                // 认证成功后没有权限操作
                .accessDeniedHandler(new CustomServerAccessDeniedHandler())
                // 还没有认证时发生认证异常，比如token过期，token不合法
                .authenticationEntryPoint(new CustomServerAuthenticationEntryPoint())
                // 将一个字符串token转换成一个认证对象
                .bearerTokenConverter(new ServerBearerTokenAuthenticationConverter())
                .and()
                .authorizeExchange()
                // 所有以 /oauth2/** 开头的请求全部放行
                .pathMatchers(skipPaths.toArray(new String[0])).permitAll()
                // 所有的请求都交由此处进行权限判断处理
                .anyExchange()
                .access(customReactiveAuthorizationManager)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomServerAccessDeniedHandler())
                .authenticationEntryPoint(new CustomServerAuthenticationEntryPoint())
                .and()
                .csrf()
                .disable()
                .addFilterBefore(new IPFilter("127.0.0.1", "0:0:0:0:0:0:0:1"), SecurityWebFiltersOrder.FIRST)
                .addFilterAfter(new RateLimitFilter(10), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAfter(new TokenTransferFilter(skipPaths), SecurityWebFiltersOrder.AUTHENTICATION);
        return http.build();
    }

    /**
     * 从jwt令牌中获取认证对象
     */
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {

        // 从jwt 中获取该令牌可以访问的权限
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // 取消权限的前缀，默认会加上SCOPE_
        authoritiesConverter.setAuthorityPrefix("");
        // 从那个字段中获取权限
        authoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        // 获取 principal name
        jwtAuthenticationConverter.setPrincipalClaimName("sub");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

    /**
     * 解码jwt
     */
    public ReactiveJwtDecoder jwtDecoder() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        Resource resource = new FileSystemResource("/Users/Dumas/WorkSpaces/MyGitProjects/spring-security-oauth-demo/sso-oauth-gateway/src/main/resources/new-authoriza-server-public-key.pem");
        String publicKeyStr = String.join("", Files.readAllLines(resource.getFile().toPath()));
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyFactory.generatePublic(keySpec);

        return NimbusReactiveJwtDecoder.withPublicKey(rsaPublicKey)
                .signatureAlgorithm(SignatureAlgorithm.RS256)
                .build();
    }
}
