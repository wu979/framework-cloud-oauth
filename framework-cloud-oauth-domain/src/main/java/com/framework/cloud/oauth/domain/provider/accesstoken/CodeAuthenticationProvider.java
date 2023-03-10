package com.framework.cloud.oauth.domain.provider.accesstoken;

import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.cache.AuthenticationCache;
import com.framework.cloud.oauth.common.model.token.CodeAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.provider.AbstractAccessTokenProvider;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;

/**
 * Created by 2022-05-10
 *
 * @author wusiwei
 * @version V1.0
 * @description: 授权码
 */
public class CodeAuthenticationProvider extends AbstractAccessTokenProvider {

    public CodeAuthenticationProvider(RedisCache redisCache, TokenGranter tokenGranter, OAuth2RequestFactory requestFactory, AuthorizationTenantService authorizationTenantService) {
        super(redisCache, tokenGranter, requestFactory, authorizationTenantService);
    }

    @Override
    protected Authentication additionalAuthenticationChecks(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.AUTHORIZATION_CODE, authentication.getPrincipal()));
        }
        if (!OauthConstant.CREDENTIALS.equals(authentication.getCredentials())) {
            throw new BadCredentialsException(OauthMsg.ERROR.getMsg());
        }
        return authentication;
    }

    @Override
    protected void pushTenantMaxCount(Long tenantId) {
        super.tenantCount(tenantId);
    }

    @Override
    protected void pushUserMaxCount(OAuth2AccessToken oAuth2AccessToken, BaseTenant baseTenant) {
        super.userCount(oAuth2AccessToken, baseTenant);
    }

    @Override
    protected Authentication createSuccessAuthentication(Authentication authentication, OAuth2AccessToken oAuth2AccessToken, AuthenticationCache authenticationCache) {
        return super.createSuccess(authenticationCache, authentication, oAuth2AccessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CodeAuthenticationModel.class.isAssignableFrom(authentication);
    }
}
