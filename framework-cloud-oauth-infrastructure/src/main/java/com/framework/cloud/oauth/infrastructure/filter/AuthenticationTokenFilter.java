package com.framework.cloud.oauth.infrastructure.filter;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.oauth.common.constant.OauthConstant;
import com.framework.cloud.oauth.common.dto.token.AccessTokenDTO;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.AuthenticationService;
import com.framework.cloud.oauth.domain.utils.HttpRequestUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Authentication token filter to obtain authorization token according to authentication type
 *
 * @author wusiwei
 */
public class AuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    private Map<String, AuthenticationService<AbstractAccessTokenModel, AccessTokenDTO>> authenticationServiceMap;

    private ObjectMapper objectMapper;

    public AuthenticationTokenFilter(String url) {
        super(new AntPathRequestMatcher(url, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        AbstractAuthenticationToken authenticationToken;
        try {
            Map<String, String> fromData = HttpRequestUtil.fromData(request);
            GrantType grantType = GrantType.grantType(fromData.get(OauthConstant.GRANT_TYPE));
            if (ObjectUtil.isNull(grantType)) {
                throw new AuthenticationServiceException(OauthMsg.GRANT_TYPE.getMsg());
            }
            String authenticationKey = grantType.name().toLowerCase();
            String json = objectMapper.writeValueAsString(fromData);
            ParameterizedType parameterizedType = FastJsonUtil.makeJavaType(grantType.clz);
            AbstractAccessTokenModel authentication = authenticationServiceMap.get(authenticationKey)
                    .authentication(FastJsonUtil.toJavaObject(json, parameterizedType));
            if (ObjectUtil.isNull(authentication)) {
                throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
            }
            authentication.setDetails(authenticationDetailsSource.buildDetails(request));
            authentication.setRequestParameters(fromData);
            authenticationToken = authentication;
        } catch (AuthenticationServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
        }
        return this.getAuthenticationManager().authenticate(authenticationToken);
    }

    public void setAuthenticationServiceMap(Map<String, AuthenticationService<AbstractAccessTokenModel, AccessTokenDTO>> authenticationServiceMap) {
        this.authenticationServiceMap = authenticationServiceMap;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
