package com.framework.cloud.oauth.common.dto.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 邮箱验证码
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailDTO extends AuthorizationDTO {
    private static final long serialVersionUID = 2456289918882947253L;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String code;
}
