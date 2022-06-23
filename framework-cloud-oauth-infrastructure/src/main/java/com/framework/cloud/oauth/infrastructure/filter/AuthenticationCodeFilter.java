package com.framework.cloud.oauth.infrastructure.filter;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.oauth.common.dto.authentication.AuthorizationDTO;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.AuthenticationService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Authorization code filter to obtain authorization code according to authentication type
 *
 * @author wusiwei
 */
public class AuthenticationCodeFilter extends AbstractAuthenticationProcessingFilter {

    private Map<String, AuthenticationService<AbstractAuthenticationModel, AuthorizationDTO>> authenticationServiceMap;

    private ObjectMapper objectMapper;

    public AuthenticationCodeFilter(String url) {
        super(new AntPathRequestMatcher(url, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        AbstractAuthenticationToken authenticationToken;
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
            String authenticationKey = grantType.name().toLowerCase();
            ParameterizedType parameterizedType = FastJsonUtil.makeJavaType(grantType.clz);
            AbstractAuthenticationToken authentication = authenticationServiceMap.get(authenticationKey)
                    .authentication(FastJsonUtil.toJavaObject(jsonObject.toJSONString(), parameterizedType));
            if (ObjectUtil.isNull(authentication)) {
                throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
            }
            authentication.setDetails(authenticationDetailsSource.buildDetails(request));
            authenticationToken = authentication;
        } catch (AuthenticationServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    public void setAuthenticationServiceMap(Map<String, AuthenticationService<AbstractAuthenticationModel, AuthorizationDTO>> authenticationServiceMap) {
        this.authenticationServiceMap = authenticationServiceMap;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
