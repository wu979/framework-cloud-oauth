package com.framework.cloud.oauth.common.rpc.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 *
 * @author wusiwei
 */
@Data
public class UserIdentifierVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "唯一标识")
    private String identifier;

    @ApiModelProperty(value = "授权凭证")
    private String credential;

}
