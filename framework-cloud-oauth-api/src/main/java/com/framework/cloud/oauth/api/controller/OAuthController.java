package com.framework.cloud.oauth.api.controller;

import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.domain.OAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "令牌检查")
    @GetMapping(value = "/check_token")
    public Result<Boolean> checkToken(@ApiParam("令牌") @RequestParam("token") String token) {
        return R.success(oAuthService.checkToken(token));
    }

}
