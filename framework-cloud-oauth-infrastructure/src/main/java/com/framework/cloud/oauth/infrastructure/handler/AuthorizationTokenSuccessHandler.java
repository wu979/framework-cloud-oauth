package com.framework.cloud.oauth.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.common.model.token.SuccessAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.common.vo.AuthorizationTokenVO;
import com.framework.cloud.oauth.infrastructure.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authorization token success processor
 *
 * @author wusiwei
 */
@RequiredArgsConstructor
public class AuthorizationTokenSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements ResponseUtil<Result<AuthorizationTokenVO>> {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (!(authentication instanceof SuccessAuthenticationModel)) {
            error(response, objectMapper, OauthMsg.ERROR.getMsg());
            return;
        }
        SuccessAuthenticationModel authenticationModel = (SuccessAuthenticationModel) authentication;
        AuthorizationTokenVO vo = new AuthorizationTokenVO(authenticationModel.getAccessToken(), authenticationModel.getRefreshToken());
        vo.setExpiresIn(authenticationModel.getExpiresIn());
        vo.setUserId(authenticationModel.getUserId());
        vo.setOauthUser(authenticationModel.getOauthUser());
        success(response, objectMapper, R.success(vo));
    }
}
