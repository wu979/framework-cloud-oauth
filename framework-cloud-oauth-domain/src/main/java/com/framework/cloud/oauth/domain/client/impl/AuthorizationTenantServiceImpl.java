package com.framework.cloud.oauth.domain.client.impl;

import com.framework.cloud.common.result.Result;
import com.framework.cloud.common.utils.AssertUtil;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.client.AuthorizationTenantService;
import com.framework.cloud.oauth.domain.convert.TenantConvert;
import com.framework.cloud.oauth.domain.feign.PlatFormFeignService;
import com.framework.cloud.platform.common.vo.TenantVO;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

/**
 * 认证租户 实现
 *
 * @author wusiwei
 */
@AllArgsConstructor
public class AuthorizationTenantServiceImpl implements AuthorizationTenantService {

    private final TenantConvert tenantConvert;
    private final PlatFormFeignService platFormFeignService;

    @Override
    public ClientDetails loadClientByClientId(String code) throws ClientRegistrationException {
        return loadTenantByCode(code);
    }

    @Override
    public BaseTenant loadTenantByCode(String code) throws InvalidClientException {
        Result<TenantVO> result = platFormFeignService.getTenant(code);
        AssertUtil.isTrue(result.success(), result);
        TenantVO tenantVO = result.getData();
        AssertUtil.isNull(tenantVO, OauthMsg.TENANT_NOT_FOUND.getMsg());
        return tenantConvert.infoToBase(tenantVO);
    }
}
