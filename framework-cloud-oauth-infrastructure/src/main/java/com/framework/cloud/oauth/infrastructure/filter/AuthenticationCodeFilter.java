package com.framework.cloud.oauth.infrastructure.filter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.oauth.common.dto.authentication.AuthorizationDTO;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.provider.AbstractAuthenticationProvider;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import com.framework.cloud.oauth.infrastructure.strategy.AuthenticationStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * Authorization code filter to obtain authorization code according to authentication type
 * The authorization code filter requests /oauth/auth to verify the user login.
 * It provides four authentication methods.
 * It filters the user information according to the selection and forwards it to the
 * {@link AbstractAuthenticationProvider AuthenticationProvider }
 *
 * @author wusiwei
 */
public class AuthenticationCodeFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private AuthenticationStrategy<AbstractAuthenticationModel, AuthorizationDTO> authenticationStrategy;

    private ObjectMapper objectMapper;

    public AuthenticationCodeFilter(String url) {
        super(new AntPathRequestMatcher(url, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        AbstractAuthenticationModel authenticationToken;
        try {
            InputStream is = request.getInputStream();
            JSONObject jsonObject = objectMapper.readValue(is, JSONObject.class);
            AuthorizationDTO authorization = jsonObject.to(AuthorizationDTO.class);
            if (ObjectUtil.isNull(authorization)) {
                throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
            }
            String appKey = authorization.getAppKey();
            String redirectUri = authorization.getRedirectUri();
            if (StringUtils.isBlank(appKey)) {
                throw new AuthenticationServiceException(MsgUtil.format(OauthMsg.APP_KEY, appKey));
            }
            if (StringUtils.isBlank(redirectUri)) {
                throw new AuthenticationServiceException(MsgUtil.format(OauthMsg.REDIRECT_URI, redirectUri));
            }
            GrantType grantType = GrantType.grantType(authorization.getAuthType());
            if (ObjectUtil.isNull(grantType)) {
                throw new AuthenticationServiceException(OauthMsg.GRANT_TYPE.getMsg());
            }
            authenticationToken = authenticationStrategy.strategy(grantType)
                    .authentication(FastJsonUtil.toJavaObject(jsonObject.toJSONString(), FastJsonUtil.makeJavaType(grantType.getClz())));
            if (ObjectUtil.isNull(authenticationToken)) {
                throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
            }
            authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
        } catch (AuthenticationServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
