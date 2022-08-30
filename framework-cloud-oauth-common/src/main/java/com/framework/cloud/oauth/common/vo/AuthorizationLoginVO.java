package com.framework.cloud.oauth.common.vo;

import com.framework.cloud.holder.model.LoginTenant;
import com.framework.cloud.holder.model.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author wusiwei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationLoginVO {


    private LoginUser loginUser;

    private LoginTenant loginTenant;

    private Set<String> roleList;
}
