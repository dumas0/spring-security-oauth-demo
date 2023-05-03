package com.dumas.sso.oauth.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/04/28 00:18
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OAuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(OAuthServerApplication.class, args);
    }
}
