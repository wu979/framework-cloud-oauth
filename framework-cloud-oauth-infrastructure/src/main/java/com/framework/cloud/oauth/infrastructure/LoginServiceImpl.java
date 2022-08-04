package com.framework.cloud.oauth.infrastructure;

import com.framework.cloud.cache.cache.RedisCache;
import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.common.utils.StringUtil;
import com.framework.cloud.holder.constant.CacheConstant;
import com.framework.cloud.holder.constant.HeaderConstant;
import com.framework.cloud.holder.model.LoginTenant;
import com.framework.cloud.holder.model.LoginUser;
import com.framework.cloud.oauth.common.vo.AuthorizationLoginVO;
import com.framework.cloud.oauth.domain.LoginService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 *
 * @author wusiwei
 */
@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final TokenStore tokenStore;
    private final RedisCache redisCache;

    @Override
    public AuthorizationLoginVO login(String authorization) {
        String accessToken = redisCache.get(CacheConstant.ACCESS_TOKEN + authorization, String.class);
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        accessToken = accessToken.replace(HeaderConstant.BEARER, "");
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
        if (oAuth2AccessToken == null) {
            return null;
        }
        if (oAuth2AccessToken.isExpired()) {
            return null;
        }
        Map<String, Object> additionalInformation = oAuth2AccessToken.getAdditionalInformation();
        String userJson = String.valueOf(additionalInformation.get(HeaderConstant.X_USER_HEADER));
        if (StringUtil.isBlank(userJson)) {
            return null;
        }
        LoginUser loginUser = FastJsonUtil.toJavaObject(userJson, LoginUser.class);
        if (null == loginUser) {
            return null;
        }
        OAuth2Authentication oauth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);
        OAuth2Request oAuth2Request = oauth2Authentication.getOAuth2Request();
        LoginTenant loginTenant = new LoginTenant(loginUser.getId(), oAuth2Request.getClientId());
        Set<String> roleList = oauth2Authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return new AuthorizationLoginVO(loginUser, loginTenant, roleList);
    }
}
