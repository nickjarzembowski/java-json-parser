package json.parser;

import java.util.List;
import java.util.Stack;

import json.domain.JsonTree;
import json.domain.JsonNode;

public class JsonParser {
    
    public static void main(String... args) {}
    
    public JsonTree parse(List<String> tokens) {
        Stack<JsonNode> stack = new Stack();
        JsonNode rootNode = new JsonNode(null);
        stack.push(rootNode);
        JsonNode parsedJsonRootNode = parseRecursive(tokens, 0, rootNode, stack);
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
    private JsonNode parseRecursive(List<String> tokens, int i, JsonNode n, Stack<JsonNode> stack) {
        if (i == tokens.size()) return stack.pop();
        String value = tokens.get(i++);
        
        if (value.equals("OBJ_OPEN")) {
            JsonNode nn = new JsonNode(n);
            n.addChild(nn);
            n = nn;
            stack.push(n);
        } else if (value.equals("KEY")) {
            n.addToMap(tokens.get(i++), null);
        } else if (value.equals("OBJ_CLOSE")) {
            n = stack.pop();
            stack.peek().addValue(n);
        } else if (value.equals("ARR_OPEN")) {
            JsonNode nn = new JsonNode(n);
            if (i == 0 || (i > 0 && tokens.get(i-1).equals("OBJ_OPEN"))){
                n.addToList(nn);
            } else {
                n.addChild(nn);
            }
            n = nn;
            stack.push(nn);
        }  else if (value.equals("ARR_VAL_END")) {
            String v = tokens.get(i-2);
            if (v.equals("OBJ_CLOSE")) {
                n.addToList(stack.pop());
            } else {
                n.addToList(v);
            }
        } else if (value.equals("ARR_CLOSE")){
            JsonNode nn = stack.pop();
            stack.peek().addValue(nn);
            n = stack.peek();
        } else if (value.equals("VAL_END")) {
            String v = tokens.get(i-2);
            if (v.equals("OBJ_CLOSE")) {
                n.addToMap(stack.pop());
            } else {
                n.addToMap(v);
            }
        }
        return parseRecursive(tokens, i, n, stack);
    }
    
}
