package com.framework.cloud.oauth.common.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 * 授权认证 模型
 *
 * @author wusiwei
 */
public class AbstractAuthenticationModel extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID = 6937430121663936838L;

    /**
     * 账户名
     */
    protected final Object principal;
    /**
     * 唯一凭证
     */
    protected Object credentials;
    /**
     * 认证租户
     */
    protected String appKey;
    /**
     * 回调地址
     */
    protected String redirectUri;
    /**
     * 其他信息
     */
    protected String state;

    public AbstractAuthenticationModel(Object principal, Object credentials, String appKey) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.appKey = appKey;
        super.setAuthenticated(true);
    }

    public AbstractAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public AbstractAuthenticationModel(Object principal, Object credentials, String appKey, String redirectUri, String state) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.appKey = appKey;
        this.redirectUri = redirectUri;
        this.state = state;
        super.setAuthenticated(true);
    }

    public AbstractAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String appKey, String redirectUri, String state) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.appKey = appKey;
        this.redirectUri = redirectUri;
        this.state = state;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

    public String getAppKey() {
        return appKey;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getState() {
        return state;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }
}
