package com.framework.cloud.oauth.domain;

import com.framework.cloud.oauth.common.vo.AuthorizationLoginVO;

/**
 * @author wusiwei
 */
public interface LoginService {

    /**
     * 获取登录认证信息
     *
     * @param authorization 令牌
     * @return 认证信息
     */
    AuthorizationLoginVO login(String authorization);
}
