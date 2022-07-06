package com.framework.cloud.oauth.domain.convert;

import com.framework.cloud.holder.model.LoginUser;
import com.framework.cloud.oauth.common.base.BaseUser;
import com.framework.cloud.oauth.common.rpc.vo.UserIdentifierVO;
import org.mapstruct.Mapper;

/**
 * 用户转换器
 *
 * @author wusiwei
 */
@Mapper(componentModel = "spring")
public interface UserConvert {

    BaseUser voToBase(UserIdentifierVO vo);

    LoginUser baseToLogin(BaseUser baseUser);
}
