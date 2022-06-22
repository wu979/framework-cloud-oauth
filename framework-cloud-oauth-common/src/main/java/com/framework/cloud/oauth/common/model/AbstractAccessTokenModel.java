package com.framework.cloud.oauth.common.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * 授权令牌 模型
 *
 * @author wusiwei
 */
public class AbstractAccessTokenModel extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID = 4502078858513645934L;

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
    protected String clientId;
    /**
     * 授权参数
     */
    private Map<String, String> requestParameters;

    public AbstractAccessTokenModel(Object principal, Object credentials, String clientId) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.clientId = clientId;
        super.setAuthenticated(true);
    }

    public AbstractAccessTokenModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String clientId) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        this.clientId = clientId;
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

    public String getClientId() {
        return clientId;
    }

    public Map<String, String> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(Map<String, String> requestParameters) {
        this.requestParameters = requestParameters;
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
