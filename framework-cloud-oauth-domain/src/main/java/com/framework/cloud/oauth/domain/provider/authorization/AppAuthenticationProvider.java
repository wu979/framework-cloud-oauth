package com.framework.cloud.oauth.domain.provider.authorization;

import com.framework.cloud.oauth.common.model.authentication.AppAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.provider.AbstractAuthenticationProvider;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by 2022-05-05
 *
 * @author wusiwei
 * @version V1.0
 * @description: 第三方open id
 */
public class AppAuthenticationProvider extends AbstractAuthenticationProvider {

    public AppAuthenticationProvider(AuthorizationUserDetailsService authorizationUserDetailsService) {
        super(authorizationUserDetailsService);
        this.hideUserNotFoundExceptions = false;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.OPEN_ID, authentication.getPrincipal()));
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        return super.createAuthentication(principal, authentication, user);
    }

    @Override
    protected UserDetails retrieveUser(String userName, Authentication authentication) throws AuthenticationException {
        return super.loadUserByTenant(userName, authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AppAuthenticationModel.class.isAssignableFrom(authentication);
    }
}
