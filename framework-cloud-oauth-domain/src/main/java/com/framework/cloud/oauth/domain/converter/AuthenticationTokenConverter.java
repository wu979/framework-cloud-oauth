package com.framework.cloud.oauth.domain.converter;

import com.framework.cloud.common.utils.FastJsonUtil;
import com.framework.cloud.holder.constant.HeaderConstant;
import com.framework.cloud.holder.model.LoginUser;
import com.framework.cloud.oauth.common.base.BaseUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author wusiwei
 */
@RequiredArgsConstructor
public class AuthenticationTokenConverter extends JwtAccessTokenConverter {

    private final UserConverter userConverter;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
        Object principal = authentication.getPrincipal();
        LoginUser loginUser = null;
        if (principal instanceof BaseUserDetail) {
            BaseUserDetail baseUserDetail = (BaseUserDetail) principal;
            loginUser = userConverter.baseToLogin(baseUserDetail.getBaseUser());
        }
        if (principal instanceof LoginUser) {
            loginUser = (LoginUser) principal;
        }
        if (null != loginUser) {
            token.getAdditionalInformation().put(HeaderConstant.X_USER_HEADER, FastJsonUtil.toJSONString(loginUser));
            token.getAdditionalInformation().put(HeaderConstant.X_USER_ID_HEADER, loginUser.getId());
        }
        return super.enhance(token, authentication);
    }
}
