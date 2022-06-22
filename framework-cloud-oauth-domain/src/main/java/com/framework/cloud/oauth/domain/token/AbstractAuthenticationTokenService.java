package com.framework.cloud.oauth.domain.token;

import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.common.utils.MD5Util;
import com.framework.cloud.common.utils.StringUtil;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.AbstractAuthorizationDTO;
import com.framework.cloud.oauth.common.dto.token.AccessTokenDTO;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.AuthenticationService;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;

import javax.annotation.Resource;

/**
 * 授权令牌抽象类
 *
 * @author wusiwei
 */
public abstract class AbstractAuthenticationTokenService<R extends AbstractAuthenticationToken, T extends AbstractAuthorizationDTO> implements AuthenticationService<R, T> {

    @Resource
    private RedisCache redisCache;

    @Resource
    private AuthorizationTenantService authorizationTenantService;

    @Override
    public R authentication(T param) {
        AccessTokenDTO authorization = (AccessTokenDTO) param;
        String clientId = authorization.getClientId();
        String clientSecret = authorization.getClientSecret();
        if (StringUtils.isBlank(clientId)) {
            throw new AuthenticationServiceException(MsgUtil.format(OauthMsg.CLIENT_ID, clientId));
        }
        if (StringUtils.isBlank(clientSecret)) {
            throw new AuthenticationServiceException(MsgUtil.format(OauthMsg.CLIENT_SECRET, clientSecret));
        }
        BaseTenant baseTenant = authorizationTenantService.loadTenantByCode(clientId);
        if (!clientSecret.equals(MD5Util.encode(clientSecret))) {
            throw new AuthenticationServiceException(MsgUtil.format(OauthMsg.CLIENT_SECRET, clientSecret));
        }
        if (!baseTenant.getAuthorizedGrantTypes().contains(authorization.getGrantType())) {
            throw new AuthenticationServiceException(MsgUtil.format(OauthMsg.GRANT_TYPE, authorization.getGrantType()));
        }
        String errorMsg = validParam(baseTenant, param);
        if (StringUtil.isNotEmpty(errorMsg)) {
            throw new AuthenticationServiceException(errorMsg);
        }
        boolean check = checkClientCount(baseTenant);
        if (!check) {
            throw new AuthenticationServiceException(OauthMsg.MAX_COUNT.getMsg());
        }
        return authenticationToken(baseTenant, param);
    }

    /**
     * 检查参数
     */
    protected abstract String validParam(BaseTenant baseTenant, T param);

    /**
     * 检查租户认证次数
     */
    protected abstract boolean checkClientCount(BaseTenant baseTenant);

    /**
     * 构建过滤链
     */
    protected abstract R authenticationToken(BaseTenant baseTenant, T param);
}
