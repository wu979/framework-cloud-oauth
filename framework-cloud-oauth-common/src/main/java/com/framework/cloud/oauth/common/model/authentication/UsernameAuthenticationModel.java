package com.framework.cloud.oauth.common.model.authentication;

import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 账号密码
 *
 * @author wusiwei
 */
public class UsernameAuthenticationModel extends AbstractAuthenticationModel {
    private static final long serialVersionUID = 8380078572690295448L;

    public UsernameAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public UsernameAuthenticationModel(Object principal, Object credentials, String appKey) {
        super(principal, credentials, appKey);
    }

    public UsernameAuthenticationModel(Object principal, Object credentials, String appKey, String redirectUri, String state) {
        super(principal, credentials, appKey, redirectUri, state);
    }

    public UsernameAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String appKey, String redirectUri, String state) {
        super(principal, credentials, authorities, appKey, redirectUri, state);
    }
}
