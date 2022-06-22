package com.framework.cloud.oauth.domain;

import com.framework.cloud.oauth.common.dto.AbstractAuthorizationDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * This interface obtains user authentication information
 *
 * @author wusiwei
 */
public interface AuthenticationService<R extends AbstractAuthenticationToken, T extends AbstractAuthorizationDTO> {

    /**
     * build user authentication information
     *
     * @param param certification parameters
     * @return authentication
     */
    R authentication(T param);

}
