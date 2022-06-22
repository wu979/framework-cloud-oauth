package com.framework.cloud.oauth.common.model.authentication;

import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 第三方
 *
 * @author wusiwei
 */
public class AppAuthenticationModel extends AbstractAuthenticationModel {
    private static final long serialVersionUID = -6848446435538469071L;

    public AppAuthenticationModel(Object principal, Object credentials, String appKey) {
        super(principal, credentials, appKey);
    }

    public AppAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public AppAuthenticationModel(Object principal, Object credentials, String appKey, String redirectUri, String state) {
        super(principal, credentials, appKey, redirectUri, state);
    }

    public AppAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String appKey, String redirectUri, String state) {
        super(principal, credentials, authorities, appKey, redirectUri, state);
    }
}
