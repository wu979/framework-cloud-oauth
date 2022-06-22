package com.framework.cloud.oauth.domain.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 授权用户信息
 *
 * @author wusiwei
 */
public interface AuthorizationUserDetailsService extends UserDetailsService {

    UserDetails loadUserByUsername(String username, Long tenantId) throws UsernameNotFoundException;
}
