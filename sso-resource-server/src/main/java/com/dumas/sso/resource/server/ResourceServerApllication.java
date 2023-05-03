package com.dumas.sso.resource.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/01 16:05
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ResourceServerApllication {
    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApllication.class, args);
    }
}
