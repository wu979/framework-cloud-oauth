package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 隐式
 *
 * @author wusiwei
 */
public class ImplicitAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = -7210976223070500218L;

    public ImplicitAuthenticationModel(Object principal, Object credentials, Long tenantId, String clientId) {
        super(principal, credentials, tenantId, clientId);
    }

    public ImplicitAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long tenantId, String clientId) {
        super(principal, credentials, authorities, tenantId, clientId);
    }
}
