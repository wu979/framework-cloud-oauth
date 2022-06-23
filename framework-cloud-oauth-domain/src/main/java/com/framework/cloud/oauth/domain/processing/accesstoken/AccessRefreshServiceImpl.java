package com.framework.cloud.oauth.domain.processing.accesstoken;

import com.framework.cloud.oauth.common.base.BaseTenant;
import com.framework.cloud.oauth.common.dto.token.RefreshDTO;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.model.token.RefreshAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.processing.AbstractAccessTokenService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 刷新
 *
 * @author wusiwei
 */
@Service("refresh_token")
public class AccessRefreshServiceImpl extends AbstractAccessTokenService<AbstractAccessTokenModel, RefreshDTO> {

    @Override
    protected String validParam(BaseTenant baseTenant, RefreshDTO param) {
        if (StringUtils.isBlank(param.getRefreshToken())) {
            return MsgUtil.format(OauthMsg.REFRESH_TOKEN, param.getRefreshToken());
        }
        return null;
    }

    @Override
    protected AbstractAccessTokenModel authenticationToken(BaseTenant baseTenant, RefreshDTO param) {
        return new RefreshAuthenticationModel(param.getRefreshToken(), "N/A", baseTenant.getClientId());
    }
}
