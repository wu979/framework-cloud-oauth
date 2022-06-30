package com.framework.cloud.oauth.infrastructure.logout;

import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.holder.constant.CacheConstant;
import com.framework.cloud.holder.constant.HeaderConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Logout
 *
 * @author wusiwei
 */
@RequiredArgsConstructor
public class AuthorizationLogoutHandler implements LogoutHandler {

    private final RedisCache redisCache;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader(HeaderConstant.AUTHORIZATION);
        if (StringUtils.isBlank(header)) {
            return;
        }
        String accessKey = CacheConstant.ACCESS_TOKEN + header;
        // token
        String accessToken = redisCache.get(accessKey, String.class);
        if (StringUtils.isBlank(accessToken)) {
            return;
        }
        if (redisCache.delete(accessKey)) {
            // 访问令牌 绑定 刷新令牌的 缓存Key
            String accessRefreshKey = CacheConstant.ACCESS_REFRESH + header;
            // refresh token
            String refreshToken = redisCache.get(accessRefreshKey, String.class);
            if (StringUtils.isNotBlank(refreshToken)) {
                redisCache.delete(accessRefreshKey, CacheConstant.REFRESH_TOKEN + refreshToken);
            }
        }
    }
}
