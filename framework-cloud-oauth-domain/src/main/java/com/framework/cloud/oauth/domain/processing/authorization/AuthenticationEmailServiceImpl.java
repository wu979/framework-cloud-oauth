package com.framework.cloud.oauth.domain.processing.authorization;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.authentication.EmailDTO;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.model.authentication.EmailAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAuthenticationService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 邮箱验证码
 *
 * @author wusiwei
 */
@Service("email")
public class AuthenticationEmailServiceImpl extends AbstractAuthenticationService<AbstractAuthenticationModel, EmailDTO> {

    @Override
    protected String validParam(EmailDTO param) {
        if (StringUtils.isBlank(param.getEmail())) {
            return MsgUtil.format(OauthMsg.EMAIL, param.getEmail());
        }
        if (StringUtils.isBlank(param.getCode())) {
            return MsgUtil.format(OauthMsg.VALID_CODE, param.getCode());
        }
        return null;
    }

    @Override
    public AbstractAuthenticationModel authenticationToken(BaseTenant baseTenant, EmailDTO param) {
        String redirectUri = param.getRedirectUri();
        String state = param.getState();
        return new EmailAuthenticationModel(param.getEmail(), param.getCode(), baseTenant.getId(), baseTenant.getClientId(), redirectUri, state);
    }
}
