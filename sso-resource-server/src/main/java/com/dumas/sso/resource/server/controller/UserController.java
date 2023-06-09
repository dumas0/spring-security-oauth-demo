package com.dumas.sso.resource.server.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/01 17:36
 */
@RestController
public class UserController {
    /**
     * 这个是受保护的资源，需要 user.userInfo 权限才可以访问。
     */
    @PreAuthorize("hasAuthority('user.userInfo')")
    @GetMapping("userInfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt principal) {
        return new HashMap<String, Object>(4) {{
            put("principal", principal);
            put("userInfo", "获取用户信息");
        }};
    }

    /**
     * 非受权限保护的资源
     */
    @GetMapping("hello")
    public String hello() {
        return "hello 不要需要受保护的资源";
    }
}
