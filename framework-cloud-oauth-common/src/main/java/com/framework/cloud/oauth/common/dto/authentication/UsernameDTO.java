package com.framework.cloud.oauth.common.dto.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 账号密码
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UsernameDTO extends AuthorizationDTO {
    private static final long serialVersionUID = 2456289918882947253L;

    /**
     * 用户名/手机号/邮箱
     */
    private String username;

    /**
     * 凭证或验证
     */
    private String password;

    /**
     * 图形验证码
     */
    private String imageCode;
}
