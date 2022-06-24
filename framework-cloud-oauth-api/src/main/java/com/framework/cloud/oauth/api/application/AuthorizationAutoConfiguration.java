package com.framework.cloud.oauth.api.application;

import com.framework.cloud.oauth.api.application.configuration.AuthorizationSecurityConfiguration;
import com.framework.cloud.oauth.api.application.configuration.AuthorizationTokenConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author wusiwei
 */
@Import({AuthorizationSecurityConfiguration.class, AuthorizationTokenConfiguration.class})
public class AuthorizationAutoConfiguration {
}
