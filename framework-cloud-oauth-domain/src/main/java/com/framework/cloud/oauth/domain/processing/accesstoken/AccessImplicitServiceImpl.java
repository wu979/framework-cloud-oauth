package com.framework.cloud.oauth.domain.processing.accesstoken;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.common.constant.OauthConstant;
import com.framework.cloud.oauth.common.dto.token.ImplicitDTO;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.ImplicitAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAccessTokenService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 隐式
 *
 * @author wusiwei
 */
@Service("implicit")
public class AccessImplicitServiceImpl extends AbstractAccessTokenService<AbstractAccessTokenModel, ImplicitDTO> {

    @Override
    protected String validParam(BaseTenant baseTenant, ImplicitDTO param) {
        if (!OauthConstant.RESPONSE_TOKEN.equals(param.getResponseType())) {
            return MsgUtil.format(OauthMsg.RESPONSE_TYPE, param.getResponseType());
        }
        if (StringUtils.isBlank(param.getScope())) {
            return MsgUtil.format(OauthMsg.SCOPE, param.getScope());
        }
        if (StringUtils.isBlank(param.getRedirectUri())) {
            return MsgUtil.format(OauthMsg.REDIRECT_URI, param.getRedirectUri());
        }
        return null;
    }

    @Override
    protected AbstractAccessTokenModel authenticationToken(BaseTenant baseTenant, ImplicitDTO param) {
        return new ImplicitAuthenticationModel(baseTenant.getClientId(), "N/A", baseTenant.getClientId());
    }
}
