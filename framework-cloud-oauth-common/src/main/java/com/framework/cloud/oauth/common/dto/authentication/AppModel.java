package com.framework.cloud.oauth.common.dto.authentication;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 第三方
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppModel extends AuthorizationDTO {
    private static final long serialVersionUID = 2456289918882947253L;

    /**
     * openID
     */
    private String openId;
}
