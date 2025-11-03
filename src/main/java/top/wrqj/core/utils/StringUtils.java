package top.wrqj.core.utils;

import java.util.HashMap;
import java.util.Map;

public class StringUtils {

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isBlank(String str) {
        return isNull(str) || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static Map<String, String> extractParams(String contentType) {
        Map<String, String> params = new HashMap<>();
        String[] split = contentType.split(";");
        if (split.length <= 1) {
            return params;
        }
        for (String item : split) {
            String param = item.trim();
            if (param.isEmpty()) {
                continue;
            }
            int index = param.indexOf("=");
            if (index > 0 && index < param.length() - 1) {
                String key = param.substring(0, index).trim();
                String value = param.substring(index + 1).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                params.put(key, value);
            } else {
                params.put(param, "");
            }
        }
        return params;
    }

}
