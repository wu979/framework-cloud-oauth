package com.framework.cloud.oauth.domain.user.impl;

import cn.hutool.core.util.ObjectUtil;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.common.base.BaseUser;
import com.framework.cloud.oauth.common.base.BaseUserDetail;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.domain.convert.UserConvert;
import com.framework.cloud.oauth.domain.feign.UserFeignService;
import com.framework.cloud.oauth.domain.user.AuthorizationUserDetailsService;
import com.framework.cloud.user.common.enums.RoleType;
import com.framework.cloud.user.common.enums.UserStatus;
import com.framework.cloud.user.common.vo.UserIdentifierVO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wusiwei
 */
public class UsernameUserDetailServiceImpl implements AuthorizationUserDetailsService {

    @Resource
    private UserFeignService userFeignService;

    @Resource
    private UserConvert userConvert;

    @Override
    public UserDetails loadUserByUsername(String username, Long tenantId) throws UsernameNotFoundException {
        Result<UserIdentifierVO> result = userFeignService.user(tenantId, username);
        if (!result.success()) {
            throw new UsernameNotFoundException(result.getMsg());
        }
        UserIdentifierVO data = result.getData();
        if (ObjectUtil.isNull(data)) {
            throw new UsernameNotFoundException(OauthMsg.USER_NOT_FOUND.getMsg());
        }
        if (!UserStatus.NORMAL.equals(data.getStatus()) || !data.getIsVerified()) {
            throw new UsernameNotFoundException(OauthMsg.USER_VERIFIED.getMsg());
        }
        if (!data.getIsBinding()) {
            throw new UsernameNotFoundException(OauthMsg.USER_BINDING.getMsg());
        }
        Result<List<String>> listResult = userFeignService.roleList(data.getId());
        if (!listResult.success()) {
            throw new UsernameNotFoundException(result.getMsg());
        }
        List<String> roleList = listResult.getData();
        BaseUser baseUser = userConvert.voToBase(data);
        List<SimpleGrantedAuthority> authorityList = roleList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        baseUser.setIsAdmin(roleList.contains(RoleType.ROLE_ADMIN.name()));
        User user = new User(data.getIdentifier(), data.getCredential(), new ArrayList<>(authorityList));
        return new BaseUserDetail(baseUser, user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException(OauthMsg.USER_NOT_FOUND.getMsg());
    }
}
