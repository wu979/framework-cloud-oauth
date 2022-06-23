package com.framework.cloud.oauth.domain.provider;

import com.framework.cloud.oauth.common.model.AbstractAuthenticationModel;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.util.Assert;

/**
 * Identity authentication processor
 * The authentication processor judges the correctness of user
 * authentication according to the authentication method selected by
 * the filter and returns the successful processor of the user according
 * to the filter Jump after successful authentication {@link AuthorizationCodeSuccessHandler }
 *
 * @author wusiwei
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected final Log logger = LogFactory.getLog(this.getClass());
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private UserCache userCache = new NullUserCache();
    private boolean forcePrincipalAsString = false;
    protected boolean hideUserNotFoundExceptions = true;
    private UserDetailsChecker preAuthenticationChecks = new AbstractAuthenticationProvider.DefaultPreAuthenticationChecks();
    private UserDetailsChecker postAuthenticationChecks = new AbstractAuthenticationProvider.DefaultPostAuthenticationChecks();
    private final AuthorizationUserDetailsService authorizationUserDetailsService;

    public AbstractAuthenticationProvider(AuthorizationUserDetailsService authorizationUserDetailsService) {
        this.authorizationUserDetailsService = authorizationUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getPrincipal() == null ? "NONE_PROVIDED" : authentication.getName();
        boolean cacheWasUsed = true;
        UserDetails user = this.userCache.getUserFromCache(username);
        if (user == null) {
            cacheWasUsed = false;
            try {
                user = this.retrieveUser(username, authentication);
            } catch (UsernameNotFoundException var6) {
                this.logger.debug("User " + username + " not found");
                if (this.hideUserNotFoundExceptions) {
                    throw new BadCredentialsException(this.messages.getMessage("AbstractAuthenticationProvider.badCredentials", "Bad credentials"));
                }
                throw var6;
            }
            Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
        }
        try {
            this.preAuthenticationChecks.check(user);
            this.additionalAuthenticationChecks(user, authentication);
        } catch (AuthenticationException var7) {
            if (!cacheWasUsed) {
                throw var7;
            }
            cacheWasUsed = false;
            user = this.retrieveUser(username, authentication);
            this.preAuthenticationChecks.check(user);
            this.additionalAuthenticationChecks(user, authentication);
        }
        this.postAuthenticationChecks.check(user);
        if (!cacheWasUsed) {
            this.userCache.putUserInCache(user);
        }
        Object principalToReturn = user;
        if (this.forcePrincipalAsString) {
            principalToReturn = user.getUsername();
        }
        return this.createSuccessAuthentication(principalToReturn, authentication, user);
    }

    protected abstract void additionalAuthenticationChecks(UserDetails var1, Authentication var2) throws AuthenticationException;

    protected abstract UserDetails retrieveUser(String var1, Authentication var2) throws AuthenticationException;

    protected abstract Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user);

    protected UserDetails loadUserByTenant(String username, Long tenantId) {
        UserDetails loadedUser;
        try {
            loadedUser = authorizationUserDetailsService.loadUserByUsername(username, tenantId);
        } catch (UsernameNotFoundException var6) {
            throw var6;
        } catch (Exception var7) {
            throw new InternalAuthenticationServiceException(OauthMsg.ERROR.getMsg());
        }
        return loadedUser;
    }

    protected UserDetails loadUserByTenant(String username, Authentication authentication) {
        return loadUserByTenant(username, ((AbstractAuthenticationModel) authentication).getTenantId());
    }

    protected Authentication createAuthentication(Object principal, Authentication authentication, UserDetails user) {
        AbstractAuthenticationModel authenticationToken = (AbstractAuthenticationModel) authentication;
        AbstractAuthenticationModel authenticationCodeToken = new AbstractAuthenticationModel(
                principal, authentication, user.getAuthorities(), authenticationToken.getTenantId(),
                authenticationToken.getAppKey(), authenticationToken.getRedirectUri(), authenticationToken.getState()
        );
        authenticationCodeToken.setDetails(authentication.getDetails());
        return authenticationCodeToken;
    }

    @Override
    public final void afterPropertiesSet() throws Exception {
        Assert.notNull(this.userCache, "A user cache must be set");
        Assert.notNull(this.messages, "A message source must be set");
        this.doAfterPropertiesSet();
    }

    public void setForcePrincipalAsString(boolean forcePrincipalAsString) {
        this.forcePrincipalAsString = forcePrincipalAsString;
    }

    public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
        this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    protected void doAfterPropertiesSet() throws Exception {
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public boolean isForcePrincipalAsString() {
        return this.forcePrincipalAsString;
    }

    public boolean isHideUserNotFoundExceptions() {
        return this.hideUserNotFoundExceptions;
    }

    protected UserDetailsChecker getPreAuthenticationChecks() {
        return this.preAuthenticationChecks;
    }

    public void setPreAuthenticationChecks(UserDetailsChecker preAuthenticationChecks) {
        this.preAuthenticationChecks = preAuthenticationChecks;
    }

    protected UserDetailsChecker getPostAuthenticationChecks() {
        return this.postAuthenticationChecks;
    }

    public void setPostAuthenticationChecks(UserDetailsChecker postAuthenticationChecks) {
        this.postAuthenticationChecks = postAuthenticationChecks;
    }

    private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
        private DefaultPostAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isCredentialsNonExpired()) {
                AbstractAuthenticationProvider.this.logger.debug("User account credentials have expired");
                throw new CredentialsExpiredException(AbstractAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
            }
        }
    }

    private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
        private DefaultPreAuthenticationChecks() {
        }

        @Override
        public void check(UserDetails user) {
            if (!user.isAccountNonLocked()) {
                AbstractAuthenticationProvider.this.logger.debug("User account is locked");
                throw new LockedException(AbstractAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"));
            } else if (!user.isEnabled()) {
                AbstractAuthenticationProvider.this.logger.debug("User account is disabled");
                throw new DisabledException(AbstractAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"));
            } else if (!user.isAccountNonExpired()) {
                AbstractAuthenticationProvider.this.logger.debug("User account is expired");
                throw new AccountExpiredException(AbstractAuthenticationProvider.this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"));
            }
        }
    }
}
