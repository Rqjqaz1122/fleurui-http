package top.wrqj.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlTemplateUtils {

    private String path;

    private final List<String> urlTemplateList;

    public UrlTemplateUtils(String path) {
        this.path = path;
        this.urlTemplateList = getTemplateStrList(path);
    }

    public static UrlTemplateUtils ofTemplate(String path) {
        return new UrlTemplateUtils(path);
    }

    public String matching(Map<String,String> map) {
        for (String s : urlTemplateList) {
            String value = map.get(s);
            this.path = this.path.replace("{" + s + "}", value);
        }
        return this.path;
    }

    public List<String> getTemplateStrList(String str) {
        Pattern pattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(str);
        List<String> placeholders = new ArrayList<>();
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
        return placeholders;
    }

    public List<String> getUrlTemplateList() {
        return this.urlTemplateList;
    }
}
