package com.framework.cloud.oauth.infrastructure.logout;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.infrastructure.response.ResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * logout success
 *
 * @author wusiwei
 */
@AllArgsConstructor
public class AuthorizationLogoutSuccessHandler implements LogoutSuccessHandler, ResponseUtil<Result<Void>> {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        success(response, objectMapper, R.success());
    }
}
