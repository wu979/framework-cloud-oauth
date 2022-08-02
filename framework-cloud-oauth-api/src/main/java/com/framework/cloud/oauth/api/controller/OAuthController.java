package com.framework.cloud.oauth.api.controller;

import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.domain.OAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wusiwei
 */
@Slf4j
@Api(tags = "认证")
@RestController
@RequestMapping(path = "/oauth")
public class OAuthController {

    @Resource
    private OAuthService oAuthService;

    @ApiOperation(value = "获取公钥")
    @PostMapping("/rsa/publicKey")
    public Result<String> getPublicKey() {
        return R.success(oAuthService.publicKey());
    }
}
