package com.framework.cloud.oauth.domain.granter.token;

import com.framework.cloud.core.spring.ApplicationContextHolder;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.authentication.UsernameAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.granter.AbstractAuthorizationGranter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
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

    public AuthorizationPasswordGranter(AuthorizationTenantService authorizationTenantService, OAuth2RequestFactory requestFactory) {
        super(GrantType.PASSWORD.name().toLowerCase(), requestFactory, authorizationTenantService);
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
            userAuth = ApplicationContextHolder.getBean(AuthenticationManager.class).authenticate(userAuth);
        } catch (Exception e) {
            throw new InvalidGrantException(e.getMessage());
        }
        if (userAuth == null || !userAuth.isAuthenticated()) {
            throw new InvalidGrantException(OauthMsg.USERNAME_PASSWORD.getMsg());
        }
        return new OAuth2Authentication(getRequestFactory().createOAuth2Request(baseTenant, tokenRequest), userAuth);
    }
}
