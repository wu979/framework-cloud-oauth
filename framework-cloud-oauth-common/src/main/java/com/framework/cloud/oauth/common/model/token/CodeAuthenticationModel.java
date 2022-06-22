package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 授权码
 *
 * @author wusiwei
 */
public class CodeAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = -6023463769139923771L;

    public CodeAuthenticationModel(Object principal, Object credentials, String clientId) {
        super(principal, credentials, clientId);
    }

    public CodeAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String clientId) {
        super(principal, credentials, authorities, clientId);
    }
}
