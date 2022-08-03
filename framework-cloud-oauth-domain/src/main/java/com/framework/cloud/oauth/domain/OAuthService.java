package com.framework.cloud.oauth.domain;

/**
 * @author wusiwei
 */
public interface OAuthService {

    /**
     * 公钥
     *
     * @return public key
     */
    String publicKey();

    /**
     * 检查令牌
     * @param token 令牌
     * @return bool
     */
    Boolean checkToken(String token);
}
