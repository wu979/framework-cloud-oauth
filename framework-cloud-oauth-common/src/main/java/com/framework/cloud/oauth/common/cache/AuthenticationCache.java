package com.framework.cloud.oauth.common.cache;

import com.framework.cloud.common.annotation.CacheTarget;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 令牌认证成功授权缓存对象
 *
 * @author wusiwei
 */
@Data
@Builder
@CacheTarget
public class AuthenticationCache implements Serializable {
    private static final long serialVersionUID = 1410525289090684753L;

    @ApiModelProperty(value = "访问令牌缓存键值")
    private String accessTokenId;

    @ApiModelProperty(value = "刷新令牌缓存键值")
    private String refreshTokenId;

    @ApiModelProperty(value = "访问令牌")
    private String accessToken;

    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;

    @ApiModelProperty(value = "访问令牌时效")
    private Integer accessTokenValidity;

    @ApiModelProperty(value = "刷新令牌时效")
    private Integer refreshTokenValidity;
}
