package com.framework.cloud.oauth.domain.processing.accesstoken;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.token.PasswordDTO;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.PasswordAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAccessTokenService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 账号密码
 *
 * @author wusiwei
 */
@Service("password")
public class AuthenticationPasswordServiceImpl extends AbstractAccessTokenService<AbstractAccessTokenModel, PasswordDTO> {

    @Override
    protected String validParam(BaseTenant baseTenant, PasswordDTO param) {
        if (StringUtils.isBlank(param.getUsername())) {
            return MsgUtil.format(OauthMsg.USERNAME, param.getUsername());
        }
        if (StringUtils.isBlank(param.getPassword())) {
            return MsgUtil.format(OauthMsg.PASSWORD, param.getPassword());
        }
        return null;
    }

    @Override
    protected AbstractAccessTokenModel authenticationToken(BaseTenant baseTenant, PasswordDTO param) {
        return new PasswordAuthenticationModel(param.getUsername(), param.getPassword(), baseTenant.getId(), baseTenant.getClientId());
    }
}
