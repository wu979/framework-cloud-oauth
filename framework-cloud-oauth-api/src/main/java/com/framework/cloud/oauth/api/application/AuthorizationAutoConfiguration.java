package com.framework.cloud.oauth.api.application;

import com.framework.cloud.oauth.api.application.configuration.AuthorizationOauth2Configuration;
import com.framework.cloud.oauth.api.application.configuration.AuthorizationSecurityConfiguration;
import com.framework.cloud.oauth.api.application.configuration.AuthorizationTokenConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author wusiwei
 */
@Import({AuthorizationOauth2Configuration.class, AuthorizationSecurityConfiguration.class, AuthorizationTokenConfiguration.class})
public class AuthorizationAutoConfiguration {
}
