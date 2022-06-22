package com.framework.cloud.oauth.common.enums;

import com.framework.cloud.oauth.common.dto.AbstractAuthorizationDTO;
import com.framework.cloud.oauth.common.dto.authentication.AppModel;
import com.framework.cloud.oauth.common.dto.authentication.EmailModel;
import com.framework.cloud.oauth.common.dto.authentication.PhoneModel;
import com.framework.cloud.oauth.common.dto.authentication.UsernameModel;
import com.framework.cloud.oauth.common.dto.token.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 认证类型
 *
 * @author wusiwei
 */
@Getter
@AllArgsConstructor
public enum GrantType {

    /** 账号密码 */
    USERNAME(UsernameModel.class),
    /** 手机验证码 */
    PHONE(PhoneModel.class),
    /** 邮箱验证码 */
    EMAIL(EmailModel.class),
    /** 三方软件 */
    APP(AppModel.class),

    /** 授权码 */
    AUTHORIZATION_CODE(CodeDTO.class),
    /** 客户端 */
    CLIENT_CREDENTIALS(CredentialsDTO.class),
    /** 隐式 */
    IMPLICIT(ImplicitDTO.class),
    /** 第三方 */
    OPEN_ID(OpenIdDTO.class),
    /** 密码 */
    PASSWORD(PasswordDTO.class),
    /** 刷新 */
    REFRESH_TOKEN(RefreshDTO.class)
    ;

    public Class<? extends AbstractAuthorizationDTO> clz;

    public static GrantType grantType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        String upperCase = type.toUpperCase();
        for (GrantType authType : GrantType.values()) {
            if (authType.name().equals(upperCase)) {
                return authType;
            }
        }
        return null;
    }
}
