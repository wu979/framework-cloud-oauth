package com.framework.cloud.oauth.api;

import com.framework.cloud.core.annotation.FrameworkApplication;
import com.framework.cloud.feign.annotation.EnableFeignInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableFeignInterceptor
@EnableDiscoveryClient
@FrameworkApplication
public class OauthApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthApiApplication.class, args);
        log.info("Oauth service started successfully");
    }

}
