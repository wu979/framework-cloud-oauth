package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 第三方
 *
 * @author wusiwei
 */
public class OpenIdAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = 7832629974840157300L;

    public OpenIdAuthenticationModel(Object principal, Object credentials, String clientId) {
        super(principal, credentials, clientId);
    }

    public OpenIdAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String clientId) {
        super(principal, credentials, authorities, clientId);
    }
}
