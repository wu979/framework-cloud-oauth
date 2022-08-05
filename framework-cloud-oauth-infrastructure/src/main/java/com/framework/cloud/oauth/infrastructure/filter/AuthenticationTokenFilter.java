package com.framework.cloud.oauth.infrastructure.filter;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.dto.token.AccessTokenDTO;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.common.model.AbstractAccessTokenModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.provider.AbstractAccessTokenProvider;
import com.framework.cloud.oauth.domain.utils.HttpRequestUtil;
import com.framework.cloud.oauth.infrastructure.strategy.AuthenticationStrategy;
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
    private AuthenticationStrategy<AbstractAccessTokenModel, AccessTokenDTO> authenticationStrategy;

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
            authenticationToken = authenticationStrategy.strategy(grantType)
                    .authentication(FastJsonUtil.toJavaObject(objectMapper.writeValueAsString(fromData), FastJsonUtil.makeJavaType(grantType.getClz())));
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
