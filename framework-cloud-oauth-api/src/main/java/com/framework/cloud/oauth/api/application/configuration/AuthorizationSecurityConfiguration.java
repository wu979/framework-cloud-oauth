package com.framework.cloud.oauth.api.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.oauth.api.application.AuthorizationAutoConfiguration;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.feign.MessageFeignService;
import com.framework.cloud.oauth.domain.properties.OauthProperties;
import com.framework.cloud.oauth.domain.provider.accesstoken.*;
import com.framework.cloud.oauth.domain.provider.authorization.AppAuthenticationProvider;
import com.framework.cloud.oauth.domain.provider.authorization.EmailAuthenticationProvider;
import com.framework.cloud.oauth.domain.provider.authorization.PhoneAuthenticationProvider;
import com.framework.cloud.oauth.domain.provider.authorization.UsernameAuthenticationProvider;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import com.framework.cloud.oauth.infrastructure.filter.AuthenticationCodeFilter;
import com.framework.cloud.oauth.infrastructure.filter.AuthenticationTokenFilter;
import com.framework.cloud.oauth.infrastructure.handler.*;
import com.framework.cloud.oauth.infrastructure.logout.AuthorizationLogoutHandler;
import com.framework.cloud.oauth.infrastructure.logout.AuthorizationLogoutSuccessHandler;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wusiwei
 */
@Configuration
@EnableWebSecurity
@AutoConfigureAfter(AuthorizationAutoConfiguration.class)
@EnableConfigurationProperties(OauthProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AuthorizationSecurityConfiguration implements Ordered {

    @Resource
    private OauthProperties oauthProperties;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private TokenGranter tokenGranter;
    @Resource
    private OAuth2RequestFactory requestFactory;
    @Resource
    private AuthorizationCodeServices authorizationCodeServices;
    @Resource
    private AuthorizationUserDetailsService authorizationUserDetailsService;
    @Resource
    private MessageFeignService messageFeignService;
    @Resource
    private AuthorizationTenantService authorizationTenantService;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(providers());
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().and()
                .authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll().and()
                .authorizeRequests().antMatchers(HttpMethod.OPTIONS).permitAll().and()
                .addFilterAt(authenticationCodeFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(authorize -> authorize
                        .antMatchers(oauthProperties.getUrl().getIgnoringUrl().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .logout().logoutUrl(oauthProperties.getUrl().getLogoutUrl()).and()
                .logout().addLogoutHandler(new AuthorizationLogoutHandler(redisCache)).and()
                .logout().logoutSuccessHandler(new AuthorizationLogoutSuccessHandler(objectMapper)).and()
                .exceptionHandling().authenticationEntryPoint(new AuthorizationPointHandler(objectMapper)).and()
                .exceptionHandling().accessDeniedHandler(new AuthorizationDeniedHandler(objectMapper)).and()
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(oauthProperties.getUrl().getIgnoringUrl().toArray(new String[0]));
    }

    @Bean
    public AuthenticationCodeFilter authenticationCodeFilter() {
        AuthenticationCodeFilter filter = new AuthenticationCodeFilter("/oauth/auth");
        filter.setObjectMapper(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthorizationCodeSuccessHandler(objectMapper, requestFactory, authorizationCodeServices));
        filter.setAuthenticationFailureHandler(new AuthorizationFailureHandler(objectMapper));
        return filter;
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() {
        AuthenticationTokenFilter filter = new AuthenticationTokenFilter("/oauth/token");
        filter.setObjectMapper(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthorizationTokenSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new AuthorizationFailureHandler(objectMapper));
        return filter;
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(oauthProperties.getUrl().getCorsPattern(), new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    public List<AuthenticationProvider> providers() {
        List<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new AppAuthenticationProvider(authorizationUserDetailsService));
        providers.add(new EmailAuthenticationProvider(authorizationUserDetailsService, messageFeignService));
        providers.add(new PhoneAuthenticationProvider(authorizationUserDetailsService, messageFeignService));
        providers.add(new UsernameAuthenticationProvider(authorizationUserDetailsService));
        providers.add(new CodeAuthenticationProvider(redisCache, tokenGranter, requestFactory, authorizationTenantService));
        providers.add(new CredentialsAuthenticationProvider(redisCache, tokenGranter, requestFactory, authorizationTenantService));
        providers.add(new ImplicitAuthenticationProvider(redisCache, tokenGranter, requestFactory, authorizationTenantService));
        providers.add(new OpenIdAuthenticationProvider(redisCache, tokenGranter, requestFactory, authorizationTenantService));
        providers.add(new PasswordAuthenticationProvider(redisCache, tokenGranter, requestFactory, authorizationTenantService));
        providers.add(new RefreshAuthenticationProvider(redisCache, tokenGranter, requestFactory, authorizationTenantService));
        return providers;
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1000;
    }
}
