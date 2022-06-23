package com.framework.cloud.oauth.domain.support.token.impl;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.token.CredentialsDTO;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.CredentialsAuthenticationModel;
import com.framework.cloud.oauth.domain.support.token.AbstractAuthenticationTokenService;
import org.springframework.stereotype.Service;

/**
 * 客户端
 *
 * @author wusiwei
 */
@Service("client_credentials")
public class AuthenticationCredentialsServiceImpl extends AbstractAuthenticationTokenService<AbstractAccessTokenModel, CredentialsDTO> {

    @Override
    protected String validParam(BaseTenant baseTenant, CredentialsDTO param) {
        return null;
    }

    @Override
    protected AbstractAccessTokenModel authenticationToken(BaseTenant baseTenant, CredentialsDTO param) {
        return new CredentialsAuthenticationModel(baseTenant.getClientId(), "N/A", baseTenant.getClientId());
    }
}
