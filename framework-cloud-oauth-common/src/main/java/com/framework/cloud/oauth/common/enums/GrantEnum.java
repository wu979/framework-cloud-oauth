package com.framework.cloud.oauth.common.enums;

import lombok.Getter;

/**
 * 授权模式
 *
 * @author wusiwei
 */
public enum GrantEnum {

    /** 授权码模式 */
    AUTHORIZATION_CODE("authorization_code"),

    /** 刷新模式 */
    REFRESH_TOKEN("refresh_token"),

    /** 客户端模式 */
    CLIENT_CREDENTIALS("client_credentials"),

    /** 密码模式 */
    PASSWORD("password"),

    /** 简化模式 */
    IMPLICIT("implicit"),

    /** 第三方模式 */
    OPEN_ID("open_id")

    ;

    @Getter
    private final String grant;

    GrantEnum(String grant) {
        this.grant = grant;
    }
}
