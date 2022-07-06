package com.framework.cloud.oauth.common.rpc.vo;

import com.framework.cloud.common.base.BaseVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 授权码 详情VO
 *
 * @author wusiwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OauthCodeInfoVO extends BaseVO {

    @ApiModelProperty(value = "授权码")
    private String code;

    @ApiModelProperty(value = "授权用户二进制信息")
    private byte[] authentication;

    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expiresTime;

}