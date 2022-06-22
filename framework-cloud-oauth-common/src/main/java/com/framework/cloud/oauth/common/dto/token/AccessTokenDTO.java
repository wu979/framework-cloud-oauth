package com.framework.cloud.oauth.common.dto.token;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.framework.cloud.oauth.common.dto.AbstractAuthorizationDTO;
import lombok.Data;

/**
 * 令牌
 *
 * @author wusiwei
 */
@Data
public class AccessTokenDTO extends AbstractAuthorizationDTO {
    private static final long serialVersionUID = -2851344385639081936L;

    /**
     * 认证类型
     */
    @JsonAlias(value = {"grant_type"})
    private String grantType;

    /**
     * 客户端
     */
    @JsonAlias(value = {"client_id"})
    private String clientId;

    /**
     * 客户端秘钥
     */
    @JsonAlias(value = {"client_secret"})
    private String clientSecret;
}
