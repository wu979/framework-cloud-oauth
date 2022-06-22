package com.framework.cloud.oauth.common.dto.token;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 授权码
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CodeDTO extends AccessTokenDTO {
    private static final long serialVersionUID = -3682063171510245219L;

    /**
     * 授权码
     */
    private String code;

    /**
     * 回调地址
     */
    @JsonAlias(value = {"redirect_uri"})
    private String redirectUri;
}
