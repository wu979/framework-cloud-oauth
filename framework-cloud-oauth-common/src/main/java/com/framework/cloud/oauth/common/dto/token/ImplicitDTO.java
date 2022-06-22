package com.framework.cloud.oauth.common.dto.token;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 隐式
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ImplicitDTO extends AccessTokenDTO {
    private static final long serialVersionUID = 3055279450254433288L;

    /**
     * 返回类型
     */
    @JsonAlias(value = {"response_type"})
    private String responseType;

    /**
     * 授权范围
     */
    private String scope;

    /**
     * 回调地址
     */
    @JsonAlias(value = {"redirect_uri"})
    private String redirectUri;
}
