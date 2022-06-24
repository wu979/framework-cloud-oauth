package com.framework.cloud.oauth.common.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wusiwei
 */
@Data
public class AuthorizationTokenVO {

    @ApiModelProperty(value = "访问令牌")
    @JSONField(name = "access_token")
    private String accessToken;

    @ApiModelProperty(value = "刷新令牌")
    @JSONField(name = "refresh_token")
    private String refreshToken;

    @ApiModelProperty(value = "访问令牌的过期时长，24小时（单位秒）")
    @JSONField(name = "expires_in")
    private String expiresIn;

    @ApiModelProperty(value = "授权者的用户ID")
    @JSONField(name = "user_id")
    private String userId;

    @ApiModelProperty(value = "授权客户端相应信息")
    @JSONField(name = "oauth_user")
    private String oauthUser;

    public AuthorizationTokenVO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
