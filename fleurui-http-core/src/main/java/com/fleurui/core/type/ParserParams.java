package com.fleurui.core.type;

import java.util.Map;

/**
 * @Author: RenQingJun
 * @Date: 2025/6/26 14:16
 * @description: 解析请求参数类型
 */
public interface ParserParams {

    Map<String,String> parseParamType(String key,Object value);

}
