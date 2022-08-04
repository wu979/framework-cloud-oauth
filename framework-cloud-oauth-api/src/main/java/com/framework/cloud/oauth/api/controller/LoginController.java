package com.framework.cloud.oauth.api.controller;

import com.framework.cloud.common.result.R;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.oauth.common.vo.AuthorizationLoginVO;
import com.framework.cloud.oauth.domain.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wusiwei
 */
@Slf4j
@Api(tags = "认证")
@RestController
@RequestMapping(path = "/oauth")
public class LoginController {

    @Resource
    private LoginService loginService;

    @ApiOperation(value = "获取认证信息")
    @PostMapping("/login")
    public Result<AuthorizationLoginVO> login(@RequestParam("authorization") String authorization) {
        return R.success(loginService.login(authorization));
    }

}
