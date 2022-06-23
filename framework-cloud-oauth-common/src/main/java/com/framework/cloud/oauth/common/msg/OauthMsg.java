package com.framework.cloud.oauth.common.msg;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 授权消息
 *
 * @author wusiwei
 */
@Getter
@AllArgsConstructor
public enum OauthMsg {

    /** 错误消息 */
    USER_NOT_FOUND("账号或密码错误"),
    USER_VERIFIED("认证后授权"),
    USER_BINDING("绑定后授权"),

    /** oauth2.0 认证失败返回消息 */
    ERROR("Authentication failed"),
    TENANT_NOT_FOUND("The tenant invalid, please contact certifying party"),
    REDIRECT_URI("Incorrect redirect_uri [{0}]"),
    CLIENT_ID("Incorrect client_id [{0}]"),
    CLIENT_SECRET("Incorrect client_secret [{0}]"),
    GRANT_TYPE("This client not supported grant type"),
    MAX_COUNT("This client has reached the maximum number of authentications"),
    APP_KEY("Incorrect app_key [{0}]"),
    CREATE_CODE("create error authorization code"),
    ERROR_CODE("Incorrect authorization code [{0}]"),
    RESPONSE_TYPE("Incorrect response_type [{0}]"),
    SCOPE("Incorrect scope [{0}]"),
    OPEN_ID("Incorrect open_id [{0}]"),
    USERNAME("Incorrect username [{0}]"),
    PASSWORD("Incorrect password [{0}]"),
    REFRESH_TOKEN("Incorrect refresh_token [{0}]"),
    EMAIL("Incorrect email [{0}]"),
    VALID_CODE("Incorrect code [{0}]"),
    PHONE("Incorrect phone [{0}]"),
    ;

    private final String msg;

}
