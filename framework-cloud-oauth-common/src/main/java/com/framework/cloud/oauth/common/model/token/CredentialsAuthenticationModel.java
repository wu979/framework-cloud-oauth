package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 客户端
 *
 * @author wusiwei
 */
public class CredentialsAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = 4213603084648368649L;

    public CredentialsAuthenticationModel(Object principal, Object credentials, String clientId) {
        super(principal, credentials, clientId);
    }

    public CredentialsAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String clientId) {
        super(principal, credentials, authorities, clientId);
    }
}
