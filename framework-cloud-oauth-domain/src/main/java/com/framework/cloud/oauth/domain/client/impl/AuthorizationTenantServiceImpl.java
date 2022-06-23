package com.framework.cloud.oauth.domain.client.impl;

import cn.hutool.core.util.ObjectUtil;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.convert.TenantConvert;
import com.framework.cloud.oauth.domain.feign.PlatFormFeignService;
import com.framework.cloud.platform.common.vo.TenantVO;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import javax.annotation.Resource;

/**
 * 认证租户 实现
 *
 * @author wusiwei
 */
public class AuthorizationTenantServiceImpl implements AuthorizationTenantService {

    @Resource
    private TenantConvert tenantConvert;

    @Resource
    private PlatFormFeignService platFormFeignService;

    @Override
    public ClientDetails loadClientByClientId(String code) throws ClientRegistrationException {
        return loadTenantByCode(code);
    }

    @Override
    public BaseTenant loadTenantByCode(String code) throws InvalidClientException {
        Result<TenantVO> result = platFormFeignService.getTenant(code);
        if (!result.success()) {
            throw new InternalAuthenticationServiceException(result.getMsg());
        }
        TenantVO tenantVO = result.getData();
        if (ObjectUtil.isNull(tenantVO)) {
            throw new InternalAuthenticationServiceException(OauthMsg.TENANT_NOT_FOUND.getMsg());
        }
        return tenantConvert.infoToBase(tenantVO);
    }
}
