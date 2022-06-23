package com.framework.cloud.oauth.domain.provider;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.authentication.AuthenticationProvider;

/**
 * Authorization token processor
 * The authorization token processor judges the user authentication
 * information according to the authentication method selected by the filter
 * It provides all authentication methods except implicit mode in oauth2.0
 * This abstract class is responsible for global call subclass authentication
 * Jump after successful authentication {@link AuthorizationTokenSuccessHandler }
 *
 * @author wusiwei
 */
public abstract class AbstractAccessTokenProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {
}
