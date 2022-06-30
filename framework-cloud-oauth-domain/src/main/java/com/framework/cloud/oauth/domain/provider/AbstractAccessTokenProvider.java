package com.framework.cloud.oauth.domain.provider;

import cn.hutool.core.util.ObjectUtil;
import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.common.enums.GlobalNumber;
import com.framework.cloud.common.utils.LocalDateUtil;
import com.framework.cloud.common.utils.UUIDUtil;
import com.framework.cloud.holder.constant.CacheConstant;
import com.framework.cloud.holder.constant.HeaderConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.cache.AuthenticationCache;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.SuccessAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Authorization token processor
 * The authorization token processor judges the user authentication
 * information according to the authentication method selected by the filter
 * It provides all authentication methods except implicit mode in oauth2.0
 * This abstract class is responsible for global call subclass authentication
 * Jump after successful authentication {@link AuthorizationTokenSuccessHandler }
 *
 * @author wusiwei
 */
public abstract class AbstractAccessTokenProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected final Log logger = LogFactory.getLog(this.getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    protected final RedisCache redisCache;
    protected final TokenGranter tokenGranter;
    protected final OAuth2RequestFactory requestFactory;
    protected final AuthorizationTenantService authorizationTenantService;

    public AbstractAccessTokenProvider(RedisCache redisCache, TokenGranter tokenGranter, OAuth2RequestFactory requestFactory, AuthorizationTenantService authorizationTenantService) {
        this.redisCache = redisCache;
        this.tokenGranter = tokenGranter;
        this.requestFactory = requestFactory;
        this.authorizationTenantService = authorizationTenantService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof AbstractAccessTokenModel)) {
            throw new InternalAuthenticationServiceException(OauthMsg.ERROR.getMsg());
        }
        OAuth2AccessToken oAuth2AccessToken;
        AuthenticationCache authenticationCache;
        try {
            authentication = additionalAuthenticationChecks(authentication);
            AbstractAccessTokenModel abstractAccessTokenModel = (AbstractAccessTokenModel) authentication;
            BaseTenant baseTenant = baseTenant(abstractAccessTokenModel);
            Map<String, String> requestParameters = requestParameters(abstractAccessTokenModel);
            TokenRequest tokenRequest = tokenRequest(requestParameters, baseTenant);
            oAuth2AccessToken = createAccessToken(tokenRequest.getGrantType(), tokenRequest);
            if (oAuth2AccessToken == null) {
                throw new InternalAuthenticationServiceException(OauthMsg.TOKEN_ERROR.getMsg());
            }
            authenticationCache = tokenCache(baseTenant, oAuth2AccessToken);
            if (!cacheAccessToken(authenticationCache, authentication)) {
                throw new InternalAuthenticationServiceException(OauthMsg.TOKEN_ERROR.getMsg());
            }
            pushTenantMaxCount(baseTenant.getId());
            pushUserMaxCount(oAuth2AccessToken, baseTenant);
        } catch (InternalAuthenticationServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
        return createSuccess(authenticationCache, authentication, oAuth2AccessToken);
    }

    /**
     * 检查
     */
    protected abstract Authentication additionalAuthenticationChecks(Authentication authentication) throws AuthenticationException;

    /**
     * 增加租户最大认证次数
     */
    protected abstract void pushTenantMaxCount(Long tenantId);

    /**
     * 增加用户最大认证次数
     */
    protected abstract void pushUserMaxCount(OAuth2AccessToken oAuth2AccessToken, BaseTenant baseTenant);

    /**
     * 创建成功授权返回信息
     */
    protected abstract Authentication createSuccessAuthentication(Authentication authentication, OAuth2AccessToken oAuth2AccessToken, AuthenticationCache authenticationCache);

    /**
     * 获取租户
     */
    protected BaseTenant baseTenant(AbstractAccessTokenModel abstractAccessTokenModel) {
        return authorizationTenantService.loadTenantByCode(abstractAccessTokenModel.getClientId());
    }

    /**
     * 获取授权参数
     */
    protected Map<String, String> requestParameters(AbstractAccessTokenModel abstractAccessTokenModel) {
        return abstractAccessTokenModel.getRequestParameters();
    }

    /**
     * 构建Oauth参数
     */
    protected TokenRequest tokenRequest(Map<String, String> requestParameters, BaseTenant baseTenant) {
        return requestFactory.createTokenRequest(requestParameters, baseTenant);
    }

    /**
     * 创建令牌
     */
    protected OAuth2AccessToken createAccessToken(String grantType, TokenRequest tokenRequest) throws AuthenticationServiceException {
        return tokenGranter.grant(grantType, tokenRequest);
    }

    /**
     * 构建令牌缓存
     */
    protected AuthenticationCache tokenCache(BaseTenant baseTenant, OAuth2AccessToken oAuth2AccessToken) {
        Integer accessTokenValiditySeconds = baseTenant.getAccessTokenValiditySeconds();
        if (ObjectUtil.isNull(accessTokenValiditySeconds) || accessTokenValiditySeconds <= GlobalNumber.ZERO.getIntValue()) {
            accessTokenValiditySeconds = -1;
        }
        Integer refreshTokenValiditySeconds = baseTenant.getRefreshTokenValiditySeconds();
        if (ObjectUtil.isNull(refreshTokenValiditySeconds) || refreshTokenValiditySeconds <= GlobalNumber.ZERO.getIntValue()) {
            refreshTokenValiditySeconds = -1;
        }
        String refreshTokenId = null;
        String refreshToken = null;
        OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
        if (ObjectUtil.isNotNull(oAuth2RefreshToken)) {
            refreshTokenId = UUIDUtil.uuid();
            refreshToken = oAuth2RefreshToken.getValue();
        }
        return AuthenticationCache.builder()
                .accessTokenId(UUIDUtil.uuid()).accessToken(oAuth2AccessToken.getValue())
                .refreshTokenId(refreshTokenId).refreshToken(refreshToken)
                .accessTokenValidity(accessTokenValiditySeconds).refreshTokenValidity(refreshTokenValiditySeconds)
                .build();
    }

    /**
     * 缓存令牌
     */
    protected boolean cacheAccessToken(AuthenticationCache authenticationCache, Authentication authentication) {
        try {
            //访问令牌 过期时间
            Integer accessValidity = authenticationCache.getAccessTokenValidity();
            //刷新令牌 过期时间
            Integer refreshValidity = authenticationCache.getRefreshTokenValidity();
            //访问令牌
            String accessToken = HeaderConstant.BEARER + authenticationCache.getAccessToken();
            //访问令牌 缓存ID
            String accessTokenId = authenticationCache.getAccessTokenId();
            //刷新令牌 缓存ID
            String refreshTokenId = authenticationCache.getRefreshTokenId();
            //访问令牌 缓存Key
            String accessKey = CacheConstant.ACCESS_TOKEN + accessTokenId;
            //刷新令牌 缓存Key
            String refreshKey = CacheConstant.REFRESH_TOKEN + refreshTokenId;
            //访问令牌 绑定 刷新令牌 缓存Key
            String accessRefreshKey = CacheConstant.ACCESS_REFRESH + accessTokenId;
            //缓存令牌
            if (accessValidity < GlobalNumber.ZERO.getIntValue()) {
                redisCache.put(accessKey, accessKey, -1);
                if (StringUtils.isNotBlank(refreshTokenId)) {
                    redisCache.put(refreshKey, authenticationCache, -1);
                    redisCache.put(accessRefreshKey, refreshTokenId, -1);
                }
            } else {
                redisCache.put(accessKey, accessToken, accessValidity, TimeUnit.SECONDS);
                if (StringUtils.isNotBlank(refreshTokenId)) {
                    redisCache.put(refreshKey, authenticationCache, refreshValidity, TimeUnit.SECONDS);
                    redisCache.put(accessRefreshKey, refreshTokenId, accessValidity, TimeUnit.SECONDS);
                }
            }
            return true;
        } catch (Exception e) {
            logger.error("缓存令牌失败:{}", e);
        }
        return false;
    }

    /**
     * 增加租户认证次数
     */
    protected void tenantCount(Long tenantId) {
        String tenantKey = CacheConstant.TENANT_COUNT + tenantId;
        Integer count = redisCache.get(tenantKey, Integer.TYPE);
        if (ObjectUtil.isNull(count)) {
            count = 1;
        } else {
            ++count;
        }
        long l = LocalDateUtil.between(LocalDateUtil.getLastDay()).toMillis();
        redisCache.put(tenantKey, count, l, TimeUnit.MILLISECONDS);
    }

    /**
     * 增加用户认证次数
     */
    protected void userCount(OAuth2AccessToken oAuth2AccessToken, BaseTenant baseTenant) {
        if (baseTenant.getMaxCount().equals(GlobalNumber.MINUS_ONE.getIntValue())) {
            return;
        }
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
        String userId = String.valueOf(additionalInformation.get(HeaderConstant.X_USER_ID_HEADER));
        String userKey = CacheConstant.TENANT_USER_COUNT + userId;
        Integer count = redisCache.get(userKey, Integer.TYPE);
        if (ObjectUtil.isNull(count)) {
            count = 1;
        } else {
            ++count;
        }
        long l = LocalDateUtil.between(LocalDateUtil.getLastDay()).toMillis();
        redisCache.put(userKey, count, l, TimeUnit.MILLISECONDS);
    }

    /**
     * 创建成功授权返回信息
     */
    protected Authentication createSuccess(AuthenticationCache authenticationCache, Authentication authentication, OAuth2AccessToken auth2AccessToken) throws AuthenticationServiceException {
        Map<String, Object> additionalInformation = auth2AccessToken.getAdditionalInformation();
        AbstractAccessTokenModel abstractAccessTokenModel = (AbstractAccessTokenModel) authentication;
        SuccessAuthenticationModel successAuthenticationModel = new SuccessAuthenticationModel(
                authentication.getPrincipal(), authentication.getCredentials(),
                abstractAccessTokenModel.getTenantId(), abstractAccessTokenModel.getClientId()
        );
        successAuthenticationModel.setAccessToken(authenticationCache.getAccessTokenId());
        successAuthenticationModel.setRefreshToken(authenticationCache.getRefreshTokenId());
        successAuthenticationModel.setExpiresIn(String.valueOf(authenticationCache.getAccessTokenValidity()));
        successAuthenticationModel.setUserId(String.valueOf(additionalInformation.get(HeaderConstant.X_USER_ID_HEADER)));
        successAuthenticationModel.setOauthUser(String.valueOf(additionalInformation.get(HeaderConstant.X_USER_HEADER)));
        return successAuthenticationModel;
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.messages, "A message source must be set");
        Assert.notNull(this.tokenGranter, "A tokenGranter source must be set");
        Assert.notNull(this.requestFactory, "A requestFactory source must be set");
        Assert.notNull(this.authorizationTenantService, "A authorizationTenantService source must be set");
        this.doAfterPropertiesSet();
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    protected void doAfterPropertiesSet() throws Exception {
    }
}
