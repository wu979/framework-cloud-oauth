package com.framework.cloud.oauth.domain.processing.accesstoken;

import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.token.CodeDTO;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.CodeAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAccessTokenService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 授权码
 *
 * @author wusiwei
 */
@Service("authorization_code")
public class AuthenticationCodeServiceImpl extends AbstractAccessTokenService<AbstractAccessTokenModel, CodeDTO> {

    @Override
    protected String validParam(BaseTenant baseTenant, CodeDTO param) {
        if (StringUtils.isBlank(param.getCode())) {
            return MsgUtil.format(OauthMsg.AUTHORIZATION_CODE, param.getCode());
        }
        if (StringUtils.isBlank(param.getRedirectUri())) {
            return MsgUtil.format(OauthMsg.REDIRECT_URI, param.getRedirectUri());
        }
        if (!baseTenant.getRegisteredRedirectUri().contains(param.getRedirectUri())) {
            return MsgUtil.format(OauthMsg.REDIRECT_URI, param.getRedirectUri());
        }
        return null;
    }

    @Override
    protected AbstractAccessTokenModel authenticationToken(BaseTenant baseTenant, CodeDTO param) {
        return new CodeAuthenticationModel(param.getCode(), OauthConstant.CREDENTIALS, baseTenant.getId(), baseTenant.getClientId());
    }

}
