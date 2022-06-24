package com.framework.cloud.oauth.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.infrastructure.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Successful acquisition of authorization code processor
 *
 * @author wusiwei
 */
@Slf4j
@RequiredArgsConstructor
public class AuthorizationCodeSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements ResponseUtil<Result<String>> {

    private final ObjectMapper objectMapper;
    private final OAuth2RequestFactory requestFactory;
    private final AuthorizationCodeServices authorizationCodeServices;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (!(authentication instanceof AbstractAuthenticationModel)) {
            error(response, objectMapper, OauthMsg.CREATE_CODE.getMsg());
            return;
        }
        AbstractAuthenticationModel authenticationModel = (AbstractAuthenticationModel) authentication;
        String appKey = authenticationModel.getAppKey();
        String redirectUri = authenticationModel.getRedirectUri();
        String state = authenticationModel.getState();
        try {
            Map<String, String> parameters = new HashMap<>(4);
            parameters.put(OAuth2Utils.CLIENT_ID, appKey);
            parameters.put(OAuth2Utils.STATE, state);
            parameters.put(OAuth2Utils.REDIRECT_URI, redirectUri);
            parameters.put(OAuth2Utils.RESPONSE_TYPE, OauthConstant.AUTHENTICATION_CODE);
            AuthorizationRequest authorizationRequest = requestFactory.createAuthorizationRequest(parameters);
            OAuth2Request oAuth2Request = requestFactory.createOAuth2Request(authorizationRequest);
            OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationModel);
            String code = authorizationCodeServices.createAuthorizationCode(oAuth2Authentication);
            if (StringUtils.isNotBlank(code)) {
                String url ;
                if (StringUtils.isBlank(state)) {
                    url = MessageFormat.format(OauthConstant.CODE_PARAM, redirectUri, appKey, code);
                } else {
                    url = MessageFormat.format(OauthConstant.CODE_PARAM_STATE, redirectUri, appKey, code, state);
                }
                success(response, objectMapper, R.success(url));
                return;
            }
        } catch (Exception e) {
            log.error("create error authorization code", e);
        }
        error(response, objectMapper, OauthMsg.CREATE_CODE.getMsg());
    }
}
