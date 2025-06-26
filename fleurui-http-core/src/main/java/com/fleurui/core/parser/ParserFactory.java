package com.fleurui.core.parser;

import java.util.ArrayList;
import java.util.List;

public class ParserFactory {

    private final List<Parser> parserList = new ArrayList<>(3);

    public ParserFactory() {
        parserList.add(new ClassParser());
        parserList.add(new MethodParser());
        parserList.add(new ParameterParser());
    }

    public void addParser(Parser parser) {
        this.parserList.add(parser);
    }

    public List<Parser> getParserList() {
        return this.parserList;
    }
}
