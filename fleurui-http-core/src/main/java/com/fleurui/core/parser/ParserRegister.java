package com.fleurui.core.parser;

import java.util.ArrayList;
import java.util.List;

public class ParserRegister {

    private final static List<Parser> parserList = new ArrayList<>();

    static {
        parserList.add(new ClassParser());
        parserList.add(new MethodParser());
        parserList.add(new ParameterParser());
    }

    public static void addParser(Parser parser) {
        parserList.add(parser);
    }


    public static List<Parser> getParserList() {
        return parserList;
    }

}
