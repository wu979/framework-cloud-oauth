package com.framework.cloud.oauth.domain.processing;

import com.framework.cloud.common.utils.StringUtil;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.authentication.AuthorizationDTO;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.AuthenticationService;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import javax.annotation.Resource;

/**
 * 认证授权抽象类
 *
 * @author wusiwei
 */
public abstract class AbstractAuthenticationService<R extends AbstractAuthenticationToken, T extends AuthorizationDTO> implements AuthenticationService<R, T> {

    @Resource
    private AuthorizationTenantService authorizationTenantService;

    @Override
    public R authentication(T param) {
        BaseTenant baseTenant = authorizationTenantService.loadTenantByCode(param.getAppKey());
        if (!baseTenant.getRegisteredRedirectUri().contains(param.getRedirectUri())) {
            throw new InternalAuthenticationServiceException(MsgUtil.format(OauthMsg.REDIRECT_URI, param.getRedirectUri()));
        }
        String errorMsg = validParam(param);
        if (StringUtil.isNotEmpty(errorMsg)) {
            throw new InternalAuthenticationServiceException(errorMsg);
        }
        return authenticationToken(baseTenant, param);
    }

    /**
     * 检查参数
     */
    protected abstract String validParam(T param);

    /**
     * 构建过滤链
     */
    protected abstract R authenticationToken(BaseTenant baseTenant, T param);
}
