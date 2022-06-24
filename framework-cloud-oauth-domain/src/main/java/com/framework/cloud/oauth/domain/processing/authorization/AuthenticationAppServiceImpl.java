package com.framework.cloud.oauth.domain.processing.authorization;

import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.authentication.AppDTO;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.model.authentication.AppAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAuthenticationService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 第三方
 *
 * @author wusiwei
 */
@Service("app")
public class AuthenticationAppServiceImpl extends AbstractAuthenticationService<AbstractAuthenticationModel, AppDTO> {

    @Override
    protected String validParam(AppDTO param) {
        if (StringUtils.isBlank(param.getOpenId())) {
            return MsgUtil.format(OauthMsg.OPEN_ID, param.getOpenId());
        }
        return null;
    }

    @Override
    public AbstractAuthenticationModel authenticationToken(BaseTenant baseTenant, AppDTO param) {
        String redirectUri = param.getRedirectUri();
        String state = param.getState();
        return new AppAuthenticationModel(param.getOpenId(), OauthConstant.CREDENTIALS, baseTenant.getId(), baseTenant.getClientId(), redirectUri, state);
    }
}
