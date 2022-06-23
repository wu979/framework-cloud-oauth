package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.common.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.model.authentication.UsernameAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import com.framework.cloud.platform.common.enums.GrantType;
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
 * Created by 2022-05-11
 *
 * @author wusiwei
 * @version V1.0
 * @description: 密码
 */
public class AuthorizationPasswordGranter extends AbstractAuthorizationGranter {

    private final AuthenticationManager authenticationManager;

    public AuthorizationPasswordGranter(AuthenticationManager authenticationManager, AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.PASSWORD.getGrant(), requestFactory, authorizationTenantService);
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected OAuth2Authentication getAuth2Authentication(BaseTenant baseTenant, TokenRequest tokenRequest) {
        Map<String, String> parameters = new LinkedHashMap<String, String>(tokenRequest.getRequestParameters());
        String clientId = parameters.get(OauthConstant.CLIENT_ID);
        String username = parameters.get(OauthConstant.USERNAME);
        String password = parameters.get(OauthConstant.PASSWORD);
        parameters.remove("password");
        Authentication userAuth = new UsernameAuthenticationModel(username, password, baseTenant.getId(), clientId);
        ((AbstractAuthenticationToken) userAuth).setDetails(parameters);
        try {
            userAuth = authenticationManager.authenticate(userAuth);
        } catch (AccountStatusException | BadCredentialsException e) {
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException(MsgUtil.format(OauthMsg.USERNAME, username));
        }
        return new OAuth2Authentication(getRequestFactory().createOAuth2Request(baseTenant, tokenRequest), userAuth);
    }
}
