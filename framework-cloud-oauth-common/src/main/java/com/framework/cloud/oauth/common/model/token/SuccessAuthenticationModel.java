package com.framework.cloud.oauth.common.model.token;

import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 授权成功
 *
 * @author wusiwei
 */
public class SuccessAuthenticationModel extends AbstractAccessTokenModel {
    private static final long serialVersionUID = 3544873076927524597L;

    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "访问令牌的过期时长，24小时（单位秒）")
    private String expiresIn;

    @ApiModelProperty(value = "授权者的用户ID")
    private String userId;

    @ApiModelProperty(value = "授权客户端相应信息")
    private String oauthUser;

    public SuccessAuthenticationModel(Object principal, Object credentials, Long tenantId, String clientId) {
        super(principal, credentials, tenantId, clientId);
    }

    public SuccessAuthenticationModel(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, Long tenantId, String clientId) {
        super(principal, credentials, authorities, tenantId, clientId);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOauthUser() {
        return oauthUser;
    }

    public void setOauthUser(String oauthUser) {
        this.oauthUser = oauthUser;
    }
}
