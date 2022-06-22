package com.framework.cloud.oauth.common.dto.token;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户端
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CredentialsDTO extends AccessTokenDTO {
    private static final long serialVersionUID = 2542385867601188093L;
}
