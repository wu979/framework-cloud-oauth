package com.framework.cloud.oauth.domain.processing.authorization;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.authentication.PhoneDTO;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.model.authentication.PhoneAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAuthenticationService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 手机验证码
 *
 * @author wusiwei
 */
@Service("phone")
public class AuthenticationPhoneServiceImpl extends AbstractAuthenticationService<AbstractAuthenticationModel, PhoneDTO> {

    @Override
    protected String validParam(PhoneDTO param) {
        if (StringUtils.isBlank(param.getPhone())) {
            return MsgUtil.format(OauthMsg.PHONE, param.getPhone());
        }
        if (StringUtils.isBlank(param.getCode())) {
            return MsgUtil.format(OauthMsg.VALID_CODE, param.getCode());
        }
        return null;
    }

    @Override
    public AbstractAuthenticationModel authenticationToken(BaseTenant baseTenant, PhoneDTO param) {
        String redirectUri = param.getRedirectUri();
        String state = param.getState();
        return new PhoneAuthenticationModel(param.getPhone(), param.getCode(), baseTenant.getId(), baseTenant.getClientId(), redirectUri, state);
    }
}
