package top.wrqj.core.type;
import top.wrqj.model.ArrayType;
import top.wrqj.model.Universal;

public class ParserParamsFactory extends AbstractParserParamsFactory {

    @Override
    public void init() {
        this.addParserParam(Universal.class, new UniversalParserAdapter());
        this.addParserParam(ArrayType.class, new ArrayParserAdapter());
        this.addParserParam(Object.class, new ObjectParserAdapter());
    }

    public ParserParamsFactory() {}

}
