package com.framework.cloud.oauth.infrastructure.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.result.R;
import com.framework.cloud.holder.constant.OauthConstant;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * response
 *
 * @author wusiwei
 */
public interface ResponseUtil<T> {

    default void success(HttpServletResponse response, ObjectMapper objectMapper, T data) throws IOException {
        response.setStatus(200);
        response.setContentType(OauthConstant.APPLICATION_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    default void error(HttpServletResponse response, ObjectMapper objectMapper, String msg) throws IOException {
        response.setStatus(200);
        response.setContentType(OauthConstant.APPLICATION_JSON);
        response.getWriter().write(objectMapper.writeValueAsString(R.error(msg)));
    }
}
