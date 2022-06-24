package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.holder.constant.HeaderConstant;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import com.framework.cloud.platform.common.enums.GrantType;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;

/**
 * Created by 2022-05-11
 *
 * @author wusiwei
 * @version V1.0
 * @description: 刷新
 */
public class AuthorizationRefreshGranter extends AbstractAuthorizationGranter {

    public AuthorizationRefreshGranter(AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.REFRESH_TOKEN.getGrant(), requestFactory, authorizationTenantService);
    }

    @Override
    protected OAuth2AccessToken getAccessToken(BaseTenant baseTenant, TokenRequest tokenRequest) {
        String refreshToken = tokenRequest.getRequestParameters().get(OauthConstant.REFRESH_TOKEN);
        if (refreshToken.startsWith(HeaderConstant.BEARER)) {
            refreshToken = refreshToken.replace(HeaderConstant.BEARER, "");
        }
        return getTokenServices(baseTenant).refreshAccessToken(refreshToken, tokenRequest);
    }
}
