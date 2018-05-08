package json.parser;

import java.util.List;
import java.util.Stack;

import json.domain.JsonNode;
import json.domain.JsonTree;
import json.lexer.domain.LexerToken;

public class JsonParser {
    
    public static void main(String... args) {}
    
    public JsonTree parse(List<String> tokens) {
        Stack<JsonNode> stack = new Stack();
        JsonNode rootNode = new JsonNode(null);
        stack.push(rootNode);
        JsonNode parsedJsonRootNode = parse(tokens, 0, rootNode, stack);
        return new JsonTree(parsedJsonRootNode);
    }
    
    /**
     * Tail recursive json parser
     * @param tokens list of lexed tokens
     * @param i index of current token
     * @param n the current node of the tree
     * @param stack the stack frame stack
     * @return
     */
    private JsonNode parse(List<String> tokens, int i, JsonNode n, Stack<JsonNode> stack) {
        if (i == tokens.size()) return stack.pop();
        String value = tokens.get(i++);
        
        if (value.equals(LexerToken.OBJ_OPEN.name())) {
            JsonNode nn = new JsonNode(n);
            n.addChild(nn);
            n = nn;
            stack.push(n);
        } else if (value.equals(LexerToken.KEY.name())) {
            n.addToMap(tokens.get(i++), null);
        } else if (value.equals(LexerToken.OBJ_CLOSE.name())) {
            n = stack.pop();
            stack.peek().addValue(n);
        } else if (value.equals(LexerToken.ARR_OPEN.name())) {
            JsonNode nn = new JsonNode(n);
            if (i == 0 || (i > 0 && tokens.get(i-1).equals(LexerToken.OBJ_OPEN.name()))){
                n.addToList(nn);
            } else {
                n.addChild(nn);
            }
            n = nn;
            stack.push(nn);
        }  else if (value.equals(LexerToken.ARR_VAL_END.name())) {
            String v = tokens.get(i-2);
            if (v.equals(LexerToken.OBJ_CLOSE.name())) {
                n.addToList(stack.pop());
            } else {
                n.addToList(v);
            }
        } else if (value.equals(LexerToken.ARR_CLOSE.name())){
            JsonNode nn = stack.pop();
            stack.peek().addValue(nn);
            n = stack.peek();
        } else if (value.equals(LexerToken.VAL_END.name())) {
            String v = tokens.get(i-2);
            if (v.equals(LexerToken.OBJ_CLOSE.name())) {
                n.addToMap(stack.pop());
            } else {
                n.addToMap(v);
            }
        }
        return parse(tokens, i, n, stack);
    }
}