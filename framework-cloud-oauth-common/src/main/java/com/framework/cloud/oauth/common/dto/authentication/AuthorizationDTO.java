package com.framework.cloud.oauth.common.dto.authentication;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.framework.cloud.oauth.common.dto.AbstractAuthorizationDTO;
import lombok.Data;

/**
 * 认证
 *
 * @author wusiwei
 */
@Data
public class AuthorizationDTO extends AbstractAuthorizationDTO {
    private static final long serialVersionUID = 2561430170566191046L;

    @JsonAlias(value = {"auth_type"})
    private String authType;

    /**
     * 租户key
     */
    private String appKey;

    /**
     * 回调地址
     */
    @JsonAlias(value = {"redirect_uri"})
    private String redirectUri;

    /**
     * 其他参数
     */
    private String state;
}
