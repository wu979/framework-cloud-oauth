package com.framework.cloud.oauth.common.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 *
 * @author wusiwei
 */
public class AbstractAuthenticationModel extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID = 6937430121663936838L;

    /**
     * 租户ID
     */
    protected Long tenantId;
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

    public AbstractAuthenticationModel(Collection<? extends GrantedAuthority> authorities, Long tenantId, String appKey, String redirectUri, String state) {
        super(authorities);
        this.tenantId = tenantId;
        this.appKey = appKey;
        this.redirectUri = redirectUri;
        this.state = state;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
