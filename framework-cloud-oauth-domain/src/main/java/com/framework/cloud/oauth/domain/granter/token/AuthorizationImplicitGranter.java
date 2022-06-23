package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import com.framework.cloud.platform.common.enums.GrantType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;

/**
 * Third party generation method
 *
 * @author wusiwei
 */
public class AuthorizationImplicitGranter extends AbstractAuthorizationGranter {

    public AuthorizationImplicitGranter(AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.IMPLICIT.getGrant(), requestFactory, authorizationTenantService);
    }

    @Override
    protected OAuth2Authentication getAuth2Authentication(BaseTenant baseTenant, TokenRequest tokenRequest) {
        Authentication userAuth = SecurityContextHolder.getContext().getAuthentication();
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InsufficientAuthenticationException("There is no currently logged in user");
        }
        OAuth2Request oAuth2Request = getRequestFactory().createOAuth2Request(baseTenant, tokenRequest);
        return new OAuth2Authentication(oAuth2Request, userAuth);

    }
}
