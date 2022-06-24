package com.framework.cloud.oauth.domain.provider.accesstoken;

import cn.hutool.core.util.ObjectUtil;
import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.common.enums.GlobalNumber;
import com.framework.cloud.holder.constant.CacheConstant;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.cache.AuthenticationCache;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.RefreshAuthenticationModel;
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

import java.util.Arrays;
import java.util.Map;

/**
 * Created by 2022-05-10
 *
 * @author wusiwei
 * @version V1.0
 * @description: 刷新
 */
public class RefreshAuthenticationProvider extends AbstractAccessTokenProvider {

    public RefreshAuthenticationProvider(RedisCache redisCache, TokenGranter tokenGranter, OAuth2RequestFactory requestFactory, AuthorizationTenantService authorizationTenantService) {
        super(redisCache, tokenGranter, requestFactory, authorizationTenantService);
    }

    @Override
    protected Authentication additionalAuthenticationChecks(Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.REFRESH_TOKEN, authentication.getPrincipal()));
        }
        if (!OauthConstant.CREDENTIALS.equals(authentication.getCredentials())) {
            throw new BadCredentialsException(OauthMsg.ERROR.getMsg());
        }
        RefreshAuthenticationModel authenticationToken = (RefreshAuthenticationModel) authentication;
        String refreshKey = CacheConstant.REFRESH_TOKEN + authentication.getPrincipal();
        AuthenticationCache authenticationCache = redisCache.get(refreshKey, AuthenticationCache.class);
        if (ObjectUtil.isNull(authenticationCache)) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.REFRESH_TOKEN_EXPIRE, authentication.getPrincipal()));
        }
        if (authenticationCache.getAccessTokenValidity() < GlobalNumber.ZERO.getIntValue()) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.REFRESH_TOKEN_EXPIRE, authentication.getPrincipal()));
        }
        authenticationToken.setOldAccessTokenId(authenticationCache.getAccessTokenId());
        authenticationToken.setOldRefreshTokenId(authenticationCache.getRefreshTokenId());
        authenticationToken.setOldAccessToken(authenticationCache.getAccessToken());
        authenticationToken.setOldRefreshToken(authenticationCache.getRefreshToken());
        authenticationToken.setOldAccessTokenValidity(authenticationCache.getAccessTokenValidity());
        authenticationToken.setOldRefreshTokenValidity(authenticationCache.getRefreshTokenValidity());
        return authenticationToken;
    }

    @Override
    protected Map<String, String> requestParameters(AbstractAccessTokenModel abstractAccessTokenModel) {
        RefreshAuthenticationModel authenticationToken = (RefreshAuthenticationModel) abstractAccessTokenModel;
        Map<String, String> requestParameters = abstractAccessTokenModel.getRequestParameters();
        requestParameters.put(OauthConstant.REFRESH_TOKEN, authenticationToken.getOldRefreshToken());
        return requestParameters;
    }

    @Override
    protected boolean cacheAccessToken(AuthenticationCache authenticationCache, Authentication authentication) {
        RefreshAuthenticationModel authenticationToken = (RefreshAuthenticationModel) authentication;
        String accessKey = CacheConstant.ACCESS_TOKEN + authenticationToken.getOldAccessTokenId();
        String refreshKey = CacheConstant.REFRESH_TOKEN + authenticationToken.getOldRefreshTokenId();
        String accessRefreshKey = CacheConstant.ACCESS_REFRESH + authenticationToken.getOldAccessTokenId();
        redisCache.delete(Arrays.asList(accessKey, refreshKey, accessRefreshKey));
        authenticationCache.setRefreshToken(authenticationToken.getOldRefreshToken());
        authenticationCache.setRefreshTokenId(authenticationToken.getOldRefreshTokenId());
        authenticationCache.setRefreshTokenValidity(authenticationToken.getOldRefreshTokenValidity());
        return super.cacheAccessToken(authenticationCache, authentication);
    }

    @Override
    protected void pushTenantMaxCount(Long tenantId) {
    }

    @Override
    protected void pushUserMaxCount(OAuth2AccessToken oAuth2AccessToken, BaseTenant baseTenant) {

    }

    @Override
    protected Authentication createSuccessAuthentication(Authentication authentication, OAuth2AccessToken oAuth2AccessToken, AuthenticationCache authenticationCache) {
        return super.createSuccess(authenticationCache, authentication, oAuth2AccessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshAuthenticationModel.class.isAssignableFrom(authentication);
    }
}
