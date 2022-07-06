package com.framework.cloud.oauth.common.rpc.vo;

import com.framework.cloud.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户 详情VO
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantVO extends BaseVO {

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "标识")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "秘钥")
    private String secret;

    @ApiModelProperty(value = "单用户最大认证次数")
    private Integer maxCount;

    @ApiModelProperty(value = "重定向URI")
    private String redirectUri;

    @ApiModelProperty(value = "授权令牌的有效时间值(单位:秒)")
    private Integer accessTokenValidity;

    @ApiModelProperty(value = "刷新令牌的有效时间值(单位:秒)")
    private Integer refreshTokenValidity;

    @ApiModelProperty(value = "申请的权限范围")
    private String scope;

    @ApiModelProperty(value = "支持授权模式")
    private String grantType;

    @ApiModelProperty(value = "客户端所拥有的Spring Security的权限值")
    private String authorities;

    @ApiModelProperty(value = "设置用户是否自动Approval操作, 默认值为 'false', 可选值包括 'true','false', 'read','write'.")
    private String approve;

    @ApiModelProperty(value = "访问的资源id集合")
    private String resourceIds;

}