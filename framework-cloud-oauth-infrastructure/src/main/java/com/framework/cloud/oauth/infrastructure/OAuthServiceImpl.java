package com.framework.cloud.oauth.infrastructure;

import cn.hutool.core.codec.Base64;
import com.framework.cloud.oauth.domain.OAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

/**
 *
 *
 * @author wusiwei
 */
@Service
@AllArgsConstructor
public class OAuthServiceImpl implements OAuthService {

    private final KeyPair keyPair;

    @Override
    public String publicKey() {
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.encode(rsaPublicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";
    }
}
