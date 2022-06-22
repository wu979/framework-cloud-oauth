package com.framework.cloud.oauth.common.dto.token;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 刷新
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RefreshDTO extends AccessTokenDTO {
    private static final long serialVersionUID = 9096414596227571674L;

    /**
     * 刷新令牌
     */
    @JsonAlias(value = {"refresh_token"})
    private String refreshToken;
}
