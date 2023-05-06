package com.dumas.sso.oauth.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/06 23:41
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OAuthGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthGatewayApplication.class, args);
    }
}
