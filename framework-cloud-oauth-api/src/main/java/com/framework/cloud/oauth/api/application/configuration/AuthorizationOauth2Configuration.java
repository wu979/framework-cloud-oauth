package com.framework.cloud.oauth.api.application.configuration;

import com.framework.cloud.oauth.domain.properties.OauthProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 *
 *
 * @author wusiwei
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
@EnableConfigurationProperties(OauthProperties.class)
public class AuthorizationOauth2Configuration extends AuthorizationServerConfigurerAdapter {



}
