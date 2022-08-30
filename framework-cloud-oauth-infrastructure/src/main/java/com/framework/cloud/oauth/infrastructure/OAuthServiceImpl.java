package com.framework.cloud.oauth.infrastructure;

import cn.hutool.core.codec.Base64;
import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.common.exception.BizException;
import com.framework.cloud.holder.constant.CacheConstant;
import com.framework.cloud.holder.constant.HeaderConstant;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.OAuthService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 * @author wusiwei
 */
@Service
@AllArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final KeyPair keyPair;
    private final TokenStore tokenStore;
    private final RedisCache redisCache;

    @Override
    public String publicKey() {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.encode(rsaPublicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";
    }

    @Override
    public Boolean checkToken(String token) {
        String accessToken = redisCache.get(CacheConstant.ACCESS_TOKEN + token, String.class);
        if (StringUtils.isBlank(accessToken)) {
            throw new BizException(OauthMsg.TOKEN_EXPIRE.getMsg());
        }
        accessToken = accessToken.replace(HeaderConstant.BEARER, "");
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            throw new BizException(OauthMsg.TOKEN_INVALID.getMsg());
        }
        if (oAuth2AccessToken.isExpired()) {
            throw new BizException(OauthMsg.TOKEN_INVALID.getMsg());
        }
        OAuth2Authentication authentication = tokenStore.readAuthentication(oAuth2AccessToken);
        return null != authentication;
    }
}
