package com.framework.cloud.oauth.common.model.authentication;

import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 邮箱验证码
 *
 * @author wusiwei
 */
public class EmailAuthenticationModel extends AbstractAuthenticationModel {
    private static final long serialVersionUID = 6600327469869091161L;

    public EmailAuthenticationModel(Object principal, Object credentials, String appKey) {
        super(principal, credentials, appKey);
    }

    public EmailAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public EmailAuthenticationModel(Object principal, Object credentials, String appKey, String redirectUri, String state) {
        super(principal, credentials, appKey, redirectUri, state);
    }

    public EmailAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String appKey, String redirectUri, String state) {
        super(principal, credentials, authorities, appKey, redirectUri, state);
    }
}
