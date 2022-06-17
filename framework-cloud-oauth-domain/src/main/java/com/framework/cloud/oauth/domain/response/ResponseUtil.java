package com.framework.cloud.oauth.domain.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.cloud.common.result.R;
import org.springframework.http.MediaType;

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
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    default void error(HttpServletResponse response, ObjectMapper objectMapper, String msg) throws IOException {
        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(R.error(msg)));
    }
}
