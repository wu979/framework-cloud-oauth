package com.framework.cloud.oauth.common.constant;

/**
 * 认证缓存
 *
 * @author wusiwei
 */
public class CacheConstant {

    /**
     * 授权令牌 (缓存key)
     */
    public static final String ACCESS_TOKEN_KEY = "token:access:";
    /**
     * 刷新令牌 (缓存key)
     */
    public static final String REFRESH_TOKEN_KEY = "token:refresh:";
    /**
     * 授权令牌绑定刷新令牌 (缓存key)
     */
    public static final String ACCESS_REFRESH_KEY = "token:access:refresh:";
}
