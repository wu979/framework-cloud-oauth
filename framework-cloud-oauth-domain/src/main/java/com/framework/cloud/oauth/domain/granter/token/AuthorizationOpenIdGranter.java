package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.common.utils.ApplicationContextHolder;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.token.OpenIdAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Client generation method
 *
 * @author wusiwei
 */
public class AuthorizationOpenIdGranter extends AbstractAuthorizationGranter {

    public AuthorizationOpenIdGranter(AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.OPEN_ID.name().toLowerCase(), requestFactory, authorizationTenantService);
    }

    @Override
    protected OAuth2Authentication getAuth2Authentication(BaseTenant baseTenant, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String clientId = parameters.get(OauthConstant.CLIENT_ID);
        String openId = parameters.get(OauthConstant.OPEN_ID);
        Authentication userAuth = new OpenIdAuthenticationModel(openId, OauthConstant.CREDENTIALS, baseTenant.getId(), clientId);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = ApplicationContextHolder.getBean(AuthenticationManager.class).authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException e) {
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException(MsgUtil.format(OauthMsg.OPEN_ID, openId));
        }
        return new OAuth2Authentication(getRequestFactory().createOAuth2Request(baseTenant, tokenRequest), userAuth);
    }
}
