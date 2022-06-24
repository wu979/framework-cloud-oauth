package com.framework.cloud.oauth.infrastructure.filter;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.dto.token.*;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.AuthenticationService;
import com.framework.cloud.oauth.domain.provider.AbstractAccessTokenProvider;
import com.framework.cloud.oauth.domain.utils.HttpRequestUtil;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Authentication token filter to obtain authorization token according to authentication type
 * The token authentication filter obtains the token through the /oauth/token interface.
 * and forwards the {@link AbstractAuthenticationToken }
 * to the authenticator {@link AbstractAccessTokenProvider AuthenticationProvider }
 *
 * @author wusiwei
 */
public class AuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private AuthenticationService<AbstractAccessTokenModel, CodeDTO> codeService;

    @Resource
    private AuthenticationService<AbstractAccessTokenModel, CredentialsDTO> credentialsService;

    @Resource
    private AuthenticationService<AbstractAccessTokenModel, ImplicitDTO> implicitService;

    @Resource
    private AuthenticationService<AbstractAccessTokenModel, OpenIdDTO> openIdService;

    @Resource
    private AuthenticationService<AbstractAccessTokenModel, PasswordDTO> passwordService;

    @Resource
    private AuthenticationService<AbstractAccessTokenModel, RefreshDTO> refreshService;

    private ObjectMapper objectMapper;

    public AuthenticationTokenFilter(String url) {
        super(new AntPathRequestMatcher(url, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!HttpMethod.POST.name().equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        AbstractAccessTokenModel authenticationToken;
        try {
            Map<String, String> fromData = HttpRequestUtil.fromData(request);
            GrantType grantType = GrantType.grantType(fromData.get(OauthConstant.GRANT_TYPE));
            if (ObjectUtil.isNull(grantType)) {
                throw new AuthenticationServiceException(OauthMsg.GRANT_TYPE.getMsg());
            }
            String dataJson = objectMapper.writeValueAsString(fromData);
            ParameterizedType parameterizedType = FastJsonUtil.makeJavaType(grantType.clz);
            switch (grantType) {
                case IMPLICIT:
                    authenticationToken = implicitService.authentication(FastJsonUtil.toJavaObject(dataJson, parameterizedType));
                    break;
                case OPEN_ID:
                    authenticationToken = openIdService.authentication(FastJsonUtil.toJavaObject(dataJson, parameterizedType));
                    break;
                case PASSWORD:
                    authenticationToken = passwordService.authentication(FastJsonUtil.toJavaObject(dataJson, parameterizedType));
                    break;
                case REFRESH_TOKEN:
                    authenticationToken = refreshService.authentication(FastJsonUtil.toJavaObject(dataJson, parameterizedType));
                    break;
                case AUTHORIZATION_CODE:
                    authenticationToken = codeService.authentication(FastJsonUtil.toJavaObject(dataJson, parameterizedType));
                    break;
                case CLIENT_CREDENTIALS:
                    authenticationToken = credentialsService.authentication(FastJsonUtil.toJavaObject(dataJson, parameterizedType));
                    break;
                default:
                    authenticationToken = null;
                    break;
            }
            if (ObjectUtil.isNull(authenticationToken)) {
                throw new AuthenticationServiceException(OauthMsg.ERROR.getMsg());
            }
            authenticationToken.setDetails(authenticationDetailsSource.buildDetails(request));
            authenticationToken.setRequestParameters(fromData);
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
