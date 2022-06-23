package com.framework.cloud.oauth.domain.granter;

import com.framework.cloud.core.spring.ApplicationContextHolder;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

import java.util.Collection;

/**
 * Token granter supports custom types
 *
 * Token generation: implement the oauth2.0 {@link TokenGranter } interface,
 * rewrite and implement the custom generation rules,
 * and adopt the prototype mode to obtain the {@link AuthorizationServerTokenServices } generated token
 *
 * @author wusiwei
 */
public abstract class AbstractAuthorizationGranter implements TokenGranter {

    private final String grantType;
    private final OAuth2RequestFactory requestFactory;
    private final AuthorizationTenantService authorizationTenantService;

    public AbstractAuthorizationGranter(String grantType, OAuth2RequestFactory requestFactory, AuthorizationTenantService authorizationTenantService) {
        this.grantType = grantType;
        this.requestFactory = requestFactory;
        this.authorizationTenantService = authorizationTenantService;
    }

    @Override
    public OAuth2AccessToken grant(String grantType, TokenRequest tokenRequest) {
        if (!this.grantType.equals(grantType)) {
            return null;
        } else {
            String clientId = tokenRequest.getClientId();
            BaseTenant baseTenant = authorizationTenantService.loadTenantByCode(clientId);
            Collection<String> authorizedGrantTypes = baseTenant.getAuthorizedGrantTypes();
            if (authorizedGrantTypes != null && !authorizedGrantTypes.isEmpty() && !authorizedGrantTypes.contains(grantType)) {
                throw new InvalidClientException("Unauthorized grant type: " + grantType);
            }
            return this.getAccessToken(baseTenant, tokenRequest);
        }
    }

    protected OAuth2AccessToken getAccessToken(BaseTenant baseTenant, TokenRequest tokenRequest) {
        return getTokenServices(baseTenant).createAccessToken(getAuth2Authentication(baseTenant, tokenRequest));
    }

    protected OAuth2Authentication getAuth2Authentication(BaseTenant baseTenant, TokenRequest tokenRequest) {
        OAuth2Request oAuth2Request = this.requestFactory.createOAuth2Request(baseTenant, tokenRequest);
        return new OAuth2Authentication(oAuth2Request, null);
    }

    protected AuthorizationServerTokenServices getTokenServices(BaseTenant baseTenant) {
        DefaultTokenServices tokenServices = ApplicationContextHolder.getBean(DefaultTokenServices.class);
        Integer accessTokenValidity = baseTenant.getAccessTokenValiditySeconds();
        boolean isRefreshToken = accessTokenValidity >= 0;
        tokenServices.setAccessTokenValiditySeconds(accessTokenValidity);
        tokenServices.setRefreshTokenValiditySeconds(baseTenant.getRefreshTokenValiditySeconds());
        tokenServices.setReuseRefreshToken(isRefreshToken);
        tokenServices.setSupportRefreshToken(isRefreshToken);
        return tokenServices;
    }

    protected OAuth2RequestFactory getRequestFactory() {
        return this.requestFactory;
    }
}
