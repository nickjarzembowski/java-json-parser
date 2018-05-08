package json.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a node in a json tree.
 * A node can be a map from string to object or a list.
 * Sub nodes can be accessed by calling getNode
 */
public class JsonNode {
    
    private Map<String, Object> map = new HashMap<>();
    private JsonNode parent;
    private List<Object> list = new ArrayList<>();
    private List<JsonNode> children = new ArrayList();
    private String lastKey = "";
    
    public JsonNode(final JsonNode parent) {
        this.parent = parent;
    }
    
    public String getKey(){
        return lastKey;
    }
    
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }
    
    public boolean containsKeys(String... keys) {
        for (String k : keys) {
            if (!containsKey(k)) {
                System.out.println("Does not contain key: " + k);
                return false;
            }
        }
        return true;
    }
    
    public JsonNode getNode(String key){
        if (map.containsKey(key) && map.get(key) instanceof JsonNode) {
            return (JsonNode) map.get(key);
        } else {
            return null;
        }
    }
    
    public String getString(String key){
        if (map.containsKey(key)) {
            return ((String) map.get(key)).replace("\"", "");
        } else {
            return null;
        }
    }
    
    public int getInt(String key){
        if (map.containsKey(key)) {
            return Integer.valueOf((String) map.get(key));
        } else {
            return 0;
        }
    }
    
    public Boolean getBoolean(String key){
        if (map.containsKey(key)) {
            return Boolean.valueOf((String)map.get(key));
        } else {
            return null;
        }
    }
    
    public double getDouble(String key){
        if (map.containsKey(key)) {
            return Double.valueOf((String) map.get(key));
        } else {
            return 0;
        }
    }
    
    public void addToMap(String key, Object value) {
        key = key.replace("\"","");
        lastKey = key;
        map.put(key,value);
    }
    
    public void addToMap(Object node) {
        map.put(lastKey, node);
    }
    
    public void addValue(Object o) {
        if (list.isEmpty() && !lastKey.isEmpty()) {
            map.put(lastKey, o);
        } else {
            list.add(o);
        }
    }
    
    public void addChild(JsonNode node) {
        children.add(node);
    }
    
    public void addToList(Object o) {
        list.add(o);
    }
    
    public List<JsonNode> getChildren() {
        return children;
    }
    
    public NodeList getNodeList() {
        return new NodeList(list);
    }
    
    public List<Integer> getIntegerList() {
        List<Integer> l = new ArrayList();
        list.forEach(s -> l.add(Integer.parseInt((String)s)));
        return l;
    }
    
    public List<String> getStringList() {
        List<String> l = new ArrayList();
        list.forEach(s -> l.add((String)s));
        return l;
    }
    
    public int getTotalKeys() {
        return map.size();
    }
    
    @Override public String toString() {
        String s = "JsonNode{" +
            "map=" + map ;
        if (!list.isEmpty()) s += "list="+list;
        return s + "}";
    }
    
    /**
     * Represents the list of objects contained within a node.
     * Used via the helper method getList in JsonNode.
     * Extends getFromFile methods in list interface for accessing list in json node.
     */
    public class NodeList {
        public List<Object> list = new ArrayList<>();
        public NodeList(final List<Object> list) {
            this.list = list;
        }
        public JsonNode getNode(int index) {
            return (JsonNode) list.get(index);
        }
        public int getInt(int i) {
            return Integer.valueOf(list.get(i).toString());
        }
        public double getDouble(int i) {
            return Double.valueOf(list.get(i).toString());
        }
        public String getString(int i) {
            return String.valueOf(list.get(i));
        }
    }
    
}