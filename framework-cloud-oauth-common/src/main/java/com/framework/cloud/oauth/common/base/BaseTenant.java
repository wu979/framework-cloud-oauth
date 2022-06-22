package com.framework.cloud.oauth.common.base;

import lombok.Data;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

/**
 * 客户端
 *
 * @author wusiwei
 */
@Data
public class BaseTenant extends BaseClientDetails {
    private static final long serialVersionUID = -4839655346573775467L;

    private Long id;

    private String name;
}
