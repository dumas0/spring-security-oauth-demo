package com.dumas.sso.oauth.server.config;

import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/19 00:50
 */
public class CustomOAuth2TokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    @Override
    public void customize(JwtEncodingContext context) {
        context.getJwsHeader().header("client-id", context.getRegisteredClient().getClientId());
        context.getJwsHeader().header("authorization-grant-type", context.getAuthorizationGrantType().getValue());

    }
}
