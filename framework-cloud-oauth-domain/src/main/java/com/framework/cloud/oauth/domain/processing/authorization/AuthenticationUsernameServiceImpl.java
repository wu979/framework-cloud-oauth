package com.framework.cloud.oauth.domain.processing.authorization;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.authentication.UsernameDTO;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.model.authentication.UsernameAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAuthenticationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 账号密码
 *
 * @author wusiwei
 */
@Service("username")
public class AuthenticationUsernameServiceImpl extends AbstractAuthenticationService<AbstractAuthenticationModel, UsernameDTO> {

    @Override
    protected String validParam(UsernameDTO param) {
        if (StringUtils.isBlank(param.getUsername())) {
            return OauthMsg.USERNAME_PASSWORD.getMsg();
        }
        if (StringUtils.isBlank(param.getPassword())) {
            return OauthMsg.USERNAME_PASSWORD.getMsg();
        }
        return null;
    }

    @Override
    public AbstractAuthenticationModel authenticationToken(BaseTenant baseTenant, UsernameDTO param) {
        String redirectUri = param.getRedirectUri();
        String state = param.getState();
        return new UsernameAuthenticationModel(param.getUsername(), param.getPassword(), baseTenant.getId(), baseTenant.getClientId(), redirectUri, state);
    }
}
