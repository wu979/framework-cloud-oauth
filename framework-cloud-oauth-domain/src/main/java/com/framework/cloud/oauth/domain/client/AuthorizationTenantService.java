package com.framework.cloud.oauth.domain.client;

import com.framework.cloud.oauth.common.base.BaseTenant;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetailsService;

/**
 * 认证租户 接口
 *
 * @author wusiwei
 */
public interface AuthorizationTenantService extends ClientDetailsService {

    BaseTenant loadTenantByCode(String code) throws InvalidClientException;

}
