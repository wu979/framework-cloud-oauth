package com.framework.cloud.oauth.domain.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.oauth.domain.response.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * authorization failure
 *
 * @author wusiwei
 */
@AllArgsConstructor
public class AuthorizationFailureHandler implements AuthenticationFailureHandler, ResponseUtil<String> {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        error(response, objectMapper, e.getMessage());
    }
}
