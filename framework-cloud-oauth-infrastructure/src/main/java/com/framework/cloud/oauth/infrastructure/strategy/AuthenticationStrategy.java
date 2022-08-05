package com.framework.cloud.oauth.infrastructure.strategy;

import com.framework.cloud.oauth.common.dto.AbstractAuthorizationDTO;
import com.framework.cloud.oauth.common.enums.GrantType;
import com.framework.cloud.oauth.domain.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wusiwei
 */
@Component
public class AuthenticationStrategy<R extends AbstractAuthenticationToken, T extends AbstractAuthorizationDTO> {

    @Autowired
    private Map<String, AuthenticationService<R, T>> authenticationServiceMap;

    public AuthenticationService<R, T> strategy(GrantType grantType) {
        return authenticationServiceMap.get(grantType.name().toLowerCase());
    }

}
