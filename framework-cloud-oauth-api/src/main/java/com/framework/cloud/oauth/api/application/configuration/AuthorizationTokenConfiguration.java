package com.framework.cloud.oauth.api.application.configuration;

import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.client.impl.AuthorizationTenantServiceImpl;
import com.framework.cloud.oauth.domain.code.AuthorizationCodeService;
import com.framework.cloud.oauth.domain.convert.AuthenticationConverter;
import com.framework.cloud.oauth.domain.convert.AuthenticationTokenConverter;
import com.framework.cloud.oauth.domain.convert.UserConvert;
import com.framework.cloud.oauth.domain.feign.PlatFormFeignService;
import com.framework.cloud.oauth.domain.granter.token.*;
import com.framework.cloud.oauth.domain.properties.OauthProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author wusiwei
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(OauthProperties.class)
public class AuthorizationTokenConfiguration {

    private final UserConvert userConvert;
    private final OauthProperties oauthProperties;
    private final PlatFormFeignService platFormFeignService;
    private final AuthenticationManager authenticationManager;

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new AuthorizationCodeService(platFormFeignService);
    }

    @Bean
    public AuthorizationTenantService authorizationTenantService() {
        return new AuthorizationTenantServiceImpl();
    }

    @Bean
    public OAuth2RequestFactory requestFactory() {
        return new DefaultOAuth2RequestFactory(authorizationTenantService());
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AuthorizationServerTokenServices authorizationServerTokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setClientDetailsService(authorizationTenantService());
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public TokenGranter tokenGranter() {
        return new CompositeTokenGranter(tokenGranterList());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        DefaultAccessTokenConverter defaultConverter = new DefaultAccessTokenConverter();
        defaultConverter.setUserTokenConverter(new AuthenticationConverter(userConvert));
        JwtAccessTokenConverter converter = new AuthenticationTokenConverter(userConvert);
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
        tokenGranters.add(new AuthorizationCodeGranter(authorizationCodeServices(), authorizationTenantService(), requestFactory()));
        // 刷新令牌模式
        tokenGranters.add(new AuthorizationRefreshGranter(authorizationTenantService(), requestFactory()));
        // 隐式授权模式
        tokenGranters.add(new AuthorizationImplicitGranter(authorizationTenantService(), requestFactory()));
        // 客户端模式
        tokenGranters.add(new AuthorizationCredentialsGranter(authorizationTenantService(), requestFactory()));
        try {
            // 密码模式
            tokenGranters.add(new AuthorizationPasswordGranter(authenticationManager, authorizationTenantService(), requestFactory()));
            // openId模式
            tokenGranters.add(new AuthorizationOpenIdGranter(authenticationManager, authorizationTenantService(), requestFactory()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenGranters;
    }
}
