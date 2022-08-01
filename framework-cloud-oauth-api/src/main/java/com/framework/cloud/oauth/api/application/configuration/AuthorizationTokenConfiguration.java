package com.framework.cloud.oauth.api.application.configuration;

import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.converter.AuthenticationConverter;
import com.framework.cloud.oauth.domain.converter.AuthenticationTokenConverter;
import com.framework.cloud.oauth.domain.converter.UserConverter;
import com.framework.cloud.oauth.domain.granter.token.*;
import com.framework.cloud.oauth.domain.properties.OauthProperties;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wusiwei
 */
@Configuration
@EnableConfigurationProperties(OauthProperties.class)
public class AuthorizationTokenConfiguration implements Ordered {

    @Resource
    private UserConverter userConverter;
    @Resource
    private OauthProperties oauthProperties;
    @Resource
    private OAuth2RequestFactory requestFactory;
    @Resource
    private AuthorizationCodeServices authorizationCodeServices;
    @Resource
    private AuthorizationTenantService authorizationTenantService;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AuthorizationServerTokenServices authorizationServerTokenServices(JwtAccessTokenConverter jwtAccessTokenConverter) {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(authorizationTenantService);
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        defaultTokenServices.setTokenStore(tokenStore(jwtAccessTokenConverter));
        return defaultTokenServices;
    }

    @Bean
    public TokenGranter tokenGranter() {
        return new CompositeTokenGranter(tokenGranterList());
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        DefaultAccessTokenConverter defaultConverter = new DefaultAccessTokenConverter();
        defaultConverter.setUserTokenConverter(new AuthenticationConverter(userConverter));
        JwtAccessTokenConverter converter = new AuthenticationTokenConverter(userConverter);
        converter.setKeyPair(keyPair());
        converter.setAccessTokenConverter(defaultConverter);
        return converter;
    }

    @Bean
    public KeyPair keyPair() {
        OauthProperties.Jwt jwt = oauthProperties.getJwt();
        return new KeyStoreKeyFactory(jwt.getKeyPath(), jwt.getPassword().toCharArray()).getKeyPair(jwt.getAlias());
    }

    private List<TokenGranter> tokenGranterList() {
        List<TokenGranter> tokenGranters = new ArrayList<>();
        // 授权码模式
        tokenGranters.add(new AuthorizationCodeGranter(authorizationCodeServices, authorizationTenantService, requestFactory));
        // 刷新令牌模式
        tokenGranters.add(new AuthorizationRefreshGranter(authorizationTenantService, requestFactory));
        // 隐式授权模式
        tokenGranters.add(new AuthorizationImplicitGranter(authorizationTenantService, requestFactory));
        // 客户端模式
        tokenGranters.add(new AuthorizationCredentialsGranter(authorizationTenantService, requestFactory));
        //密码模式
        tokenGranters.add(new AuthorizationPasswordGranter(authorizationTenantService, requestFactory));
        // openId模式
        tokenGranters.add(new AuthorizationOpenIdGranter(authorizationTenantService, requestFactory));
        return tokenGranters;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
