package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 密码
 *
 * @author wusiwei
 */
public class PasswordAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = 2128883647329835369L;

    public PasswordAuthenticationModel(Object principal, Object credentials, String clientId) {
        super(principal, credentials, clientId);
    }

    public PasswordAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String clientId) {
        super(principal, credentials, authorities, clientId);
    }
}
