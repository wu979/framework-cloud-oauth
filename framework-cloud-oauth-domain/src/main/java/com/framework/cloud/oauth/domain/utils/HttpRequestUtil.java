package com.framework.cloud.oauth.domain.utils;

import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wusiwei
 */
public class HttpRequestUtil {

    /**
     * 获取表单数据
     *
     * @param request 前端域
     * @return 表单数据
     */
    public static Map<String, String> fromData(@NonNull HttpServletRequest request) {
        Map<String, String> fromData = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        if (null != parameterNames) {
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = request.getParameter(key);
                fromData.put(key, value);
            }
        }
        return fromData;
    }

}
