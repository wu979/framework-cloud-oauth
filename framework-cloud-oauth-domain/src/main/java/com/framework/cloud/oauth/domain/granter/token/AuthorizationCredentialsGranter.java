package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;

/**
 * Client generation method
 *
 * @author wusiwei
 */
public class AuthorizationCredentialsGranter extends AbstractAuthorizationGranter {

    private boolean allowRefresh = false;

    public AuthorizationCredentialsGranter(AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.CLIENT_CREDENTIALS.name().toLowerCase(), requestFactory, authorizationTenantService);
    }

    @Override
    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        OAuth2AccessToken token = super.grant(grantType, tokenRequest);
        if (token != null) {
            DefaultOAuth2AccessToken norefresh = new DefaultOAuth2AccessToken(token);
            if (!allowRefresh) {
                norefresh.setRefreshToken(null);
            }
            token = norefresh;
        }
        return token;
    }

    public void setAllowRefresh(boolean allowRefresh) {
        this.allowRefresh = allowRefresh;
    }
}
