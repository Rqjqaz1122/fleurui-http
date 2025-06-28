package com.fleurui.core.type;

import java.util.Map;

public interface ParserParams {

    Map<String,String> parseParamType(String key,Object value);

}
