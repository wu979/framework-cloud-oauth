package com.framework.cloud.oauth.domain.code;

import cn.hutool.core.util.ObjectUtil;
import com.framework.cloud.common.result.Result;
import com.framework.cloud.holder.constant.OauthConstant;
import com.framework.cloud.oauth.common.msg.OauthMsg;
import com.framework.cloud.oauth.common.rpc.dto.OauthCodeDTO;
import com.framework.cloud.oauth.common.rpc.vo.OauthCodeInfoVO;
import com.framework.cloud.oauth.domain.feign.PlatFormFeignService;
import com.framework.cloud.oauth.domain.utils.MsgUtil;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

/**
 * 授权码生成
 *
 * @author wusiwei
 */
public class AuthorizationCodeService extends RandomValueAuthorizationCodeServices {

    private final RandomValueStringGenerator generator;
    private final PlatFormFeignService platFormFeignService;

    public AuthorizationCodeService(PlatFormFeignService platFormFeignService) {
        this.generator = new RandomValueStringGenerator(OauthConstant.CODE_RANDOM);
        this.platFormFeignService = platFormFeignService;
    }

    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        byte[] serialize = SerializationUtils.serialize(authentication);
        Result<Boolean> result = platFormFeignService.save(new OauthCodeDTO(code, serialize));
        if (!result.success()) {
            throw new InvalidGrantException(result.getMsg());
        }
        if (!result.getData()) {
            throw new InvalidGrantException(OauthMsg.CREATE_CODE.getMsg());
        }
    }

    @Override
    public OAuth2Authentication remove(String code) {
        Result<OauthCodeInfoVO> result = platFormFeignService.getOauthCode(code);
        if (!result.success()) {
            throw new InvalidGrantException(MsgUtil.format(OauthMsg.AUTHORIZATION_CODE, code));
        }
        OauthCodeInfoVO oauthCodeInfoVO = result.getData();
        if (ObjectUtil.isNull(oauthCodeInfoVO)) {
            throw new InvalidGrantException(MsgUtil.format(OauthMsg.AUTHORIZATION_CODE, code));
        }
        try {
            byte[] bytes = oauthCodeInfoVO.getAuthentication();
            return (OAuth2Authentication) SerializationUtils.deserialize(bytes);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidGrantException(OauthMsg.ERROR.getMsg());
        }
    }

    @Override
    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = this.generator.generate();
        this.store(code, authentication);
        return code;
    }

}
