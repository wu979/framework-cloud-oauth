package com.framework.cloud.oauth.common.rpc.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权码 新增DTO
 *
 * @author wusiwei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OauthCodeDTO {

    @ApiModelProperty(value = "授权码")
    private String code;

    @ApiModelProperty(value = "授权用户信息")
    private byte[] authentication;

    @ApiModelProperty(value = "租户")
    private Long tenantId;
}
