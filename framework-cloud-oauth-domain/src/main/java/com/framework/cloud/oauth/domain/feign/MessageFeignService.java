package com.framework.cloud.oauth.domain.feign;

import com.framework.cloud.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 消息
 *
 * @author wusiwei
 */
@FeignClient(contextId = "messageFeignService", value = "framework-cloud-message-api", decode404 = true)
public interface MessageFeignService {

    @GetMapping(value = "/{number}/{code}/authentication")
    Result<Boolean> authentication(@PathVariable("number") String number, @PathVariable("code") String code);

}
