package com.framework.cloud.oauth.api.application.configuration;

import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.properties.OauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

/**
 * @author wusiwei
 */
@Configuration
@EnableConfigurationProperties(OauthProperties.class)
@Import({AuthorizationServerEndpointsConfiguration.class, AuthorizationServerConfigurerAdapter.class})
public class AuthorizationOauthConfiguration extends AuthorizationServerConfigurerAdapter {

    @Resource
    private TokenStore tokenStore;
    @Resource
    private TokenGranter tokenGranter;
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Resource
    private AuthorizationTenantService authorizationTenantService;
    @Resource
    private AuthorizationCodeServices authorizationCodeServices;
    @Resource
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(authorizationTenantService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()");
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .authorizationCodeServices(authorizationCodeServices)
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(jwtAccessTokenConverter)
                .tokenServices(authorizationServerTokenServices)
                .tokenStore(tokenStore)
                .tokenGranter(tokenGranter)
        ;
    }

}
