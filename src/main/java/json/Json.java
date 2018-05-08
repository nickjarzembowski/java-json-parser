package json;

import json.lexer.JsonLexer;
import json.parser.JsonParser;
import json.util.FileUtil;

public class Json {
    
    private JsonLexer lexer;
    private JsonParser parser;
    private FileUtil fileUtil;
    
    public Json() {
        lexer = new JsonLexer();
        parser = new JsonParser();
        fileUtil = new FileUtil();
    }
    
    public json.domain.Json getFromFile(String fileName) {
        return parser.parse(lexer.lex(fileUtil.getFileAsString(fileName)));
    }
    
    public json.domain.Json getFromString(String json) {
        return parser.parse(lexer.lex(json));
    }
}