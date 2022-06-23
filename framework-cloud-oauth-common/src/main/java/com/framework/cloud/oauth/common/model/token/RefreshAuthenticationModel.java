package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 刷新
 *
 * @author wusiwei
 */
public class RefreshAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = 4544593109603809639L;

    /**
     * 刷新前访问令牌缓存键值
     */
    private String oldAccessTokenId;

    /**
     * 刷新前刷新令牌缓存键值
     */
    private String oldRefreshTokenId;

    /**
     * 刷新前访问令牌
     */
    private String oldAccessToken;

    /**
     * 刷新前刷新令牌
     */
    private String oldRefreshToken;

    /**
     * 刷新前访问令牌时
     */
    private Integer oldAccessTokenValidity;

    /**
     * 刷新前刷新令牌时
     */
    private Integer oldRefreshTokenValidity;

    public RefreshAuthenticationModel(Object principal, Object credentials, Long tenantId, String clientId) {
        super(principal, credentials, tenantId, clientId);
    }

    public RefreshAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long tenantId, String clientId) {
        super(principal, credentials, authorities, tenantId, clientId);
    }

    public String getOldAccessTokenId() {
        return oldAccessTokenId;
    }

    public void setOldAccessTokenId(String oldAccessTokenId) {
        this.oldAccessTokenId = oldAccessTokenId;
    }

    public String getOldRefreshTokenId() {
        return oldRefreshTokenId;
    }

    public void setOldRefreshTokenId(String oldRefreshTokenId) {
        this.oldRefreshTokenId = oldRefreshTokenId;
    }

    public String getOldAccessToken() {
        return oldAccessToken;
    }

    public void setOldAccessToken(String oldAccessToken) {
        this.oldAccessToken = oldAccessToken;
    }

    public String getOldRefreshToken() {
        return oldRefreshToken;
    }

    public void setOldRefreshToken(String oldRefreshToken) {
        this.oldRefreshToken = oldRefreshToken;
    }

    public Integer getOldAccessTokenValidity() {
        return oldAccessTokenValidity;
    }

    public void setOldAccessTokenValidity(Integer oldAccessTokenValidity) {
        this.oldAccessTokenValidity = oldAccessTokenValidity;
    }

    public Integer getOldRefreshTokenValidity() {
        return oldRefreshTokenValidity;
    }

    public void setOldRefreshTokenValidity(Integer oldRefreshTokenValidity) {
        this.oldRefreshTokenValidity = oldRefreshTokenValidity;
    }
}
