package top.wrqj.core.type;
import top.wrqj.model.ArrayType;

public class ParserParamsFactory extends AbstractParserParamsFactory {

    @Override
    public void init() {
        this.addParserParam(Number.class, new NumberParserAdapter());
        this.addParserParam(ArrayType.class, new ArrayParserAdapter());
        this.addParserParam(Object.class, new ObjectParserAdapter());
    }

    public ParserParamsFactory() {}

}
