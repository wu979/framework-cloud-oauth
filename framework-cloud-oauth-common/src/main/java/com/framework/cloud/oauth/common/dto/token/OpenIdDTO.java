package com.framework.cloud.oauth.common.dto.token;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 第三方
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenIdDTO extends AccessTokenDTO {
    private static final long serialVersionUID = 4480949035542559111L;

    /**
     * 第三方开放ID
     */
    @JsonAlias(value = {"open_id"})
    private String openId;
}
