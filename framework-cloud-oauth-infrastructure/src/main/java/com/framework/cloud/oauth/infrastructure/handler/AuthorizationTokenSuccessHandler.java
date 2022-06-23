package com.framework.cloud.oauth.infrastructure.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.infrastructure.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token acquisition success processor
 *
 * @author wusiwei
 */
@RequiredArgsConstructor
public class AuthorizationTokenSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements ResponseUtil<Result<String>> {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        success(response, objectMapper, R.success(""));
    }
}
