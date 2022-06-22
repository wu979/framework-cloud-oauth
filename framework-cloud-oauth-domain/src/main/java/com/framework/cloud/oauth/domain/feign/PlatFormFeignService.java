package com.framework.cloud.oauth.domain.feign;

import com.framework.cloud.common.result.Result;
import com.framework.cloud.platform.common.dto.OauthCodeDTO;
import com.framework.cloud.platform.common.vo.OauthCodeInfoVO;
import com.framework.cloud.platform.common.vo.TenantVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 平台
 *
 * @author wusiwei
 */
@FeignClient(contextId = "platFormFeignService", value = "framework-cloud-platform-api", decode404 = true)
public interface PlatFormFeignService {

    @GetMapping(value = "/tenant/{code}/info/code")
    Result<TenantVO> getTenant(@PathVariable("code") String code);

    @GetMapping(value = "/oauth-code/{code}/info")
    Result<OauthCodeInfoVO> getOauthCode(@PathVariable("code") String code);

    @PostMapping(value = "/oauth-code/save")
    Result<Boolean> save(OauthCodeDTO param);
}
