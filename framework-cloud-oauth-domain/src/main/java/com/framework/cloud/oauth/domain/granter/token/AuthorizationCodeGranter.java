package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.common.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import com.framework.cloud.platform.common.enums.GrantType;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

import java.util.HashMap;
import java.util.Map;

/**
 * Authorization code generation method
 *
 * @author wusiwei
 */
public class AuthorizationCodeGranter extends AbstractAuthorizationGranter {

    private final AuthorizationCodeServices authorizationCodeServices;

    public AuthorizationCodeGranter(AuthorizationCodeServices authorizationCodeServices, AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.AUTHORIZATION_CODE.getGrant(), requestFactory, authorizationTenantService);
        this.authorizationCodeServices = authorizationCodeServices;
    }

    @Override
    protected OAuth2Authentication getAuth2Authentication(BaseTenant baseTenant, TokenRequest tokenRequest) {
        Map<String, String> requestParameters = tokenRequest.getRequestParameters();
        String code = requestParameters.get(OauthConstant.AUTHENTICATION_CODE);
        String redirectUri = requestParameters.get(OauthConstant.REDIRECT_URI);
        OAuth2Authentication oAuth2Authentication = authorizationCodeServices.consumeAuthorizationCode(code);
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        if (!oAuth2Request.getRedirectUri().equals(redirectUri)) {
            throw new RedirectMismatchException(MsgUtil.format(OauthMsg.REDIRECT_URI, redirectUri));
        }
        String clientId = tokenRequest.getClientId();
        if (!clientId.equals(oAuth2Request.getClientId())) {
            throw new RedirectMismatchException(MsgUtil.format(OauthMsg.CLIENT_ID, clientId));
        }
        Map<String, String> combinedParameters = new HashMap<String, String>(oAuth2Request.getRequestParameters());
        combinedParameters.putAll(requestParameters);
        return new OAuth2Authentication(oAuth2Request.createOAuth2Request(combinedParameters), oAuth2Authentication.getUserAuthentication());
    }
}
