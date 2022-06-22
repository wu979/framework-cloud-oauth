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
    ERROR("authentication failed"),
    TENANT_NOT_FOUND("The tenant invalid, please contact certifying party"),
    REDIRECT_URI("Incorrect redirect_uri [{0}]"),
    CLIENT_ID("Incorrect client_id [{0}]"),
    CLIENT_SECRET("Incorrect client_secret [{0}]"),
    GRANT_TYPE("This client not supported grant type"),
    MAX_COUNT("This client has reached the maximum number of authentications"),
    APP_KEY("Incorrect app_key [{0}]"),
    CREATE_CODE("create error authorization code"),
    ERROR_CODE("Incorrect authorization code [{0}]"),
    USER_NOT_FOUND("账号或密码错误"),
    USER_VERIFIED("认证后授权"),
    USER_BINDING("绑定后授权"),
    ;

    private final String msg;

}
