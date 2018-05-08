package json;

import json.domain.JsonTree;
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
    
    public JsonTree getFromFile(String fileName) {
        JsonTree tree = null;
        try {
            tree = parser.parse(lexer.lex(fileUtil.getFileAsString(fileName)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }
    
    public JsonTree getFromString(String json) {
        JsonTree tree = null;
        try {
            tree = parser.parse(lexer.lex(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }
}