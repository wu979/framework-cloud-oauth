package com.framework.cloud.oauth.common.dto.token;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 密码
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PasswordDTO extends AccessTokenDTO {
    private static final long serialVersionUID = 5908238693808543118L;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
