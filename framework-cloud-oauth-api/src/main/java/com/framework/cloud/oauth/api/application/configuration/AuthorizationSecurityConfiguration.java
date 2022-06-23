package com.framework.cloud.oauth.api.application.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.client.impl.AuthorizationTenantServiceImpl;
import com.framework.cloud.oauth.domain.properties.OauthProperties;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import com.framework.cloud.oauth.domain.user.impl.UsernameUserDetailServiceImpl;
import com.framework.cloud.oauth.infrastructure.filter.AuthenticationCodeFilter;
import com.framework.cloud.oauth.infrastructure.filter.AuthenticationTokenFilter;
import com.framework.cloud.oauth.infrastructure.handler.*;
import com.framework.cloud.oauth.infrastructure.logout.AuthorizationLogoutHandler;
import com.framework.cloud.oauth.infrastructure.logout.AuthorizationLogoutSuccessHandler;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;

/**
 *
 *
 * @author wusiwei
 */
@EnableWebSecurity
@EnableConfigurationProperties(OauthProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AuthorizationSecurityConfiguration {

    @Resource
    private OauthProperties oauthProperties;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private RedisCache redisCache;
    @Resource
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                //关闭csrf
                .csrf().disable()
                //关闭session
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
    public AuthenticationCodeFilter authenticationCodeFilter() throws Exception {
        AuthenticationCodeFilter filter = new AuthenticationCodeFilter("/oauth/auth");
        filter.setObjectMapper(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthorizationCodeSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new AuthorizationFailureHandler(objectMapper));
        return filter;
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() throws Exception {
        AuthenticationTokenFilter filter = new AuthenticationTokenFilter("/oauth/token");
        filter.setObjectMapper(objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new AuthorizationTokenSuccessHandler(objectMapper));
        filter.setAuthenticationFailureHandler(new AuthorizationFailureHandler(objectMapper));
        return filter;
    }

    @Bean
    public AuthorizationUserDetailsService authorizationUserDetailsService() {
        return new UsernameUserDetailServiceImpl();
    }

    @Bean
    public AuthorizationTenantService authorizationTenantService() {
        return new AuthorizationTenantServiceImpl();
    }

    /** 跨源访问 */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(oauthProperties.getUrl().getCorsPattern(), new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
