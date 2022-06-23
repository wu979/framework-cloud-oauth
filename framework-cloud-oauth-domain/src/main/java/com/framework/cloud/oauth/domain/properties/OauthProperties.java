package com.framework.cloud.oauth.domain.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * oauth
 *
 * @author wusiwei
 */
@Data
@ConfigurationProperties(prefix = "framework.oauth2")
public class OauthProperties {

    @ApiModelProperty(value = "单租户最大认证次数")
    private Integer maxCount;

    private Url url;

    private Jwt jwt;

    @Data
    @ApiModel(value = "路径信息")
    public static class Url {

        @ApiModelProperty(value = "cors")
        private String corsPattern;

        @ApiModelProperty(value = "登出")
        private String logoutUrl;

        @ApiModelProperty(value = "鉴权排除")
        private List<String> ignoringUrl;

    }

    @Data
    @ApiModel(value = "令牌信息")
    public static class Jwt {

        @ApiModelProperty(value = "签名")
        private String keyPath;

        @ApiModelProperty(value = "别名")
        private String alias;

        @ApiModelProperty(value = "密码")
        private String password;

    }
}
