package com.framework.cloud.oauth.common.model.authentication;

import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 手机验证码
 *
 * @author wusiwei
 */
public class PhoneAuthenticationModel extends AbstractAuthenticationModel {
    private static final long serialVersionUID = -7936028866461437776L;

    public PhoneAuthenticationModel(Object principal, Object credentials, String appKey) {
        super(principal, credentials, appKey);
    }

    public PhoneAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public PhoneAuthenticationModel(Object principal, Object credentials, String appKey, String redirectUri, String state) {
        super(principal, credentials, appKey, redirectUri, state);
    }

    public PhoneAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String appKey, String redirectUri, String state) {
        super(principal, credentials, authorities, appKey, redirectUri, state);
    }
}
