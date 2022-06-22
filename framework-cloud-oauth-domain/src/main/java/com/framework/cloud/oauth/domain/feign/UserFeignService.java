package com.framework.cloud.oauth.domain.feign;

import com.framework.cloud.common.result.Result;
import com.framework.cloud.user.common.vo.UserIdentifierVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 用户
 *
 * @author wusiwei
 */
@FeignClient(contextId = "userFeignService", value = "framework-cloud-user-api", decode404 = true)
public interface UserFeignService {

    @GetMapping(value = "/user/{tenantId}/{identifier}/user")
    Result<UserIdentifierVO> user(@PathVariable("tenantId") Long tenantId, @PathVariable("identifier") String identifier);

    @GetMapping(value = "/{userId}/role/list")
    Result<List<String>> roleList(@PathVariable("userId") Long userId);
}
