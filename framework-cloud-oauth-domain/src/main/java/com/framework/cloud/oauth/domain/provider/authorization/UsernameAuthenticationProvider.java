package com.framework.cloud.oauth.domain.provider.authorization;

import com.framework.cloud.common.utils.MD5Util;
import com.framework.cloud.oauth.common.model.authentication.UsernameAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.provider.AbstractAuthenticationProvider;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @program: wsw-starter-cloud
 * @author: wsw
 * @create: 2021-05-31 09:56
 * @description: 账号密码
 **/
public class UsernameAuthenticationProvider extends AbstractAuthenticationProvider {

    public UsernameAuthenticationProvider(AuthorizationUserDetailsService authorizationUserDetailsService) {
        super(authorizationUserDetailsService);
        this.hideUserNotFoundExceptions = false;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.USERNAME, authentication.getPrincipal()));
        }
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.PASSWORD, authentication.getCredentials()));
        }
        String password = authentication.getCredentials().toString();
        if (!userDetails.getPassword().equals(MD5Util.encode(password))) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.PASSWORD, authentication.getCredentials()));
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
        return UsernameAuthenticationModel.class.isAssignableFrom(authentication);
    }

}
