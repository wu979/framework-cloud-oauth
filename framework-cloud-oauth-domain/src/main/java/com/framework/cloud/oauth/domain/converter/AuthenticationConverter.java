package com.framework.cloud.oauth.domain.converter;

import cn.hutool.core.collection.CollectionUtil;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.holder.model.LoginUser;
import com.framework.cloud.oauth.common.base.BaseUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wusiwei
 */
@RequiredArgsConstructor
public class AuthenticationConverter extends DefaultUserAuthenticationConverter {

    private final UserConverter userConverter;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (CollectionUtil.isNotEmpty(authorities)) {
            response.put(OauthConstant.AUTHORITIES, AuthorityUtils.authorityListToSet(authorities));
        }
        Object principal = authentication.getPrincipal();
        LoginUser loginUser = null;
        if (principal instanceof BaseUserDetail) {
            BaseUserDetail baseUserDetail = (BaseUserDetail) principal;
            loginUser = userConverter.baseToLogin(baseUserDetail.getBaseUser());
        }
        if (principal instanceof LoginUser) {
            loginUser = (LoginUser) principal;
        }
        response.put(OauthConstant.USER_DETAIL, loginUser);
        response.put(OauthConstant.USER_NAME, authentication.getName());
        return response;
    }

}
