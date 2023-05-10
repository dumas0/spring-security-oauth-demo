package com.dumas.sso.oauth.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/06 23:42
 */
@SpringBootTest
public class OAuthGatewayApplicationTest {
    @Test
    public void test() {
        List<String> skipPaths = new ArrayList<>(Arrays.asList("/oauth2/**", "/login",
                "/doc.html/**", "/webjars/**", "/swagger-ui.html", "/v2/**", "/swagger-resources", "swagger-resources/**",
                "/favicon.ico"));
        String path = "/user/oauth2/token";
        boolean match = skipPaths.stream()
                .map(skipPath -> skipPath.replaceAll("\\*", ".*"))
                .anyMatch(path::matches);
        System.out.println(match);
    }
}
