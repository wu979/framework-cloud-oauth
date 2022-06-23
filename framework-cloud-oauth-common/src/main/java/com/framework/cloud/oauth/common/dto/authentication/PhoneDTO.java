package com.framework.cloud.oauth.common.dto.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 手机验证码
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PhoneDTO extends AuthorizationDTO {
    private static final long serialVersionUID = 2456289918882947253L;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;
}
