package com.framework.cloud.oauth.domain.support.authorization.impl;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.authentication.UsernameDTO;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.model.authentication.UsernameAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.support.authorization.AbstractAuthenticationService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
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
            return MsgUtil.format(OauthMsg.USERNAME, param.getUsername());
        }
        if (StringUtils.isBlank(param.getPassword())) {
            return MsgUtil.format(OauthMsg.PASSWORD, param.getPassword());
        }
        return null;
    }

    @Override
    public AbstractAuthenticationModel authenticationToken(BaseTenant baseTenant, UsernameDTO param) {
        String redirectUri = param.getRedirectUri();
        String state = param.getState();
        return new UsernameAuthenticationModel(param.getUsername(), param.getPassword(), baseTenant.getClientId(), redirectUri, state);
    }
}
