package com.framework.cloud.oauth.common.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 认证用户
 *
 * @author wusiwei
 */
@Data
public class BaseUser implements Serializable {
    private static final long serialVersionUID = 8459488853854275522L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "是否超级管理员")
    private Boolean isAdmin;

    @ApiModelProperty(value = "租户id")
    private Long tenantId;

}
