package com.framework.cloud.oauth.domain.provider.authorization;

import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.common.model.authentication.PhoneAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.feign.MessageFeignService;
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
 * @description: 手机验证码 验证器
 **/
public class PhoneAuthenticationProvider extends AbstractAuthenticationProvider {

    private final MessageFeignService messageFeignService;

    public PhoneAuthenticationProvider(AuthorizationUserDetailsService authorizationUserDetailsService, MessageFeignService messageFeignService) {
        super(authorizationUserDetailsService);
        this.hideUserNotFoundExceptions = false;
        this.messageFeignService = messageFeignService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, Authentication authentication) throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.PHONE, authentication.getPrincipal()));
        }
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.VALID_CODE, authentication.getCredentials()));
        }
        String phone = authentication.getPrincipal().toString();
        String code = authentication.getCredentials().toString();
        Result<Boolean> result = messageFeignService.authentication(phone, code);
        if (!result.success() || !result.getData()) {
            throw new BadCredentialsException(MsgUtil.format(OauthMsg.VALID_CODE, authentication.getCredentials()));
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
        return PhoneAuthenticationModel.class.isAssignableFrom(authentication);
    }
}
