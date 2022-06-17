package com.framework.cloud.oauth.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.oauth.domain.handler.AuthorizationDeniedHandler;
import com.framework.cloud.oauth.domain.handler.AuthorizationPointHandler;
import com.framework.cloud.oauth.domain.logout.AuthorizationLogoutHandler;
import com.framework.cloud.oauth.domain.logout.AuthorizationLogoutSuccessHandler;
import com.framework.cloud.oauth.domain.properties.OauthProperties;
import lombok.AllArgsConstructor;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 *
 * @author wusiwei
 */
@AllArgsConstructor
@EnableWebSecurity
@EnableConfigurationProperties(OauthProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class AuthorizationSecurityConfiguration {

    private final OauthProperties oauthProperties;
    private final ObjectMapper objectMapper;
    private final RedisCache redisCache;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
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


    /** 跨源访问 */
    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(oauthProperties.getUrl().getCorsPattern(), new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
