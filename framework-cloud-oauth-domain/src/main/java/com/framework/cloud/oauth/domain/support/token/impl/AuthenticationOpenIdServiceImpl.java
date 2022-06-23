package com.framework.cloud.oauth.domain.support.token.impl;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.token.OpenIdDTO;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.OpenIdAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.support.token.AbstractAuthenticationTokenService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 第三方
 *
 * @author wusiwei
 */
@Service("open_id")
public class AuthenticationOpenIdServiceImpl extends AbstractAuthenticationTokenService<AbstractAccessTokenModel, OpenIdDTO> {

    @Override
    protected String validParam(BaseTenant baseTenant, OpenIdDTO param) {
        if (StringUtils.isBlank(param.getOpenId())) {
            return MsgUtil.format(OauthMsg.OPEN_ID, param.getOpenId());
        }
        return null;
    }

    @Override
    protected AbstractAccessTokenModel authenticationToken(BaseTenant baseTenant, OpenIdDTO param) {
        return new OpenIdAuthenticationModel(param.getOpenId(), "N/A", baseTenant.getClientId());
    }
}
