package com.framework.cloud.oauth.domain.utils;

import com.framework.cloud.oauth.common.msg.OauthMsg;

import java.text.MessageFormat;

/**
 * 消息格式化
 *
 * @author wusiwei
 */
public class MsgUtil {

    public static String format(OauthMsg oauthMsg, Object... value) {
        return MessageFormat.format(oauthMsg.getMsg(), value);
    }
}
