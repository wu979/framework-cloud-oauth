package com.framework.cloud.oauth.api.application;

import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.client.impl.AuthorizationTenantServiceImpl;
import com.framework.cloud.oauth.domain.code.AuthorizationCodeService;
import com.framework.cloud.oauth.domain.feign.PlatFormFeignService;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import com.framework.cloud.oauth.domain.user.impl.UsernameUserDetailServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;

import javax.annotation.Resource;

/**
 * @author wusiwei
 */
@Configuration
@EnableGlobalAuthentication
public class AuthorizationAutoConfiguration implements Ordered {

    @Resource
    private PlatFormFeignService platFormFeignService;

    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new AuthorizationCodeService(platFormFeignService);
    }

    @Bean
    public AuthorizationUserDetailsService authorizationUserDetailsService() {
        return new UsernameUserDetailServiceImpl();
    }

    @Bean
    public AuthorizationTenantService authorizationTenantService() {
        return new AuthorizationTenantServiceImpl();
    }

    @Bean
    public OAuth2RequestFactory requestFactory(AuthorizationTenantService authorizationTenantService) {
        return new DefaultOAuth2RequestFactory(authorizationTenantService);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}
