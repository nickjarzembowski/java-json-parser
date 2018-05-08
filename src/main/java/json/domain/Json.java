package json.domain;

import java.util.List;

/**
 * Contains the root node of a json tree
 */
public class JsonTree {
    
    private final JsonNode root;
    
    public JsonTree(final JsonNode root) {
        this.root = root;
    }
    public JsonNode getRoot() { return root.getChildren().get(0); }
    
    public JsonNode getNode(String key) { return getRoot().getNode(key); }
    
    public JsonNode.NodeList getNodeList(String key) { return getRoot().getNode(key).getNodeList(); }
    
    public List<String> getStringList(String key) { return getRoot().getNode(key).getStringList(); }
    
    public List<Integer> getIntegerList(String key) { return getRoot().getNode(key).getIntegerList(); }
    
    public boolean containsKey(String key) { return getRoot().containsKey(key); }
    
    public boolean containsKeys(String... keys) { return getRoot().containsKeys(keys); }
    
    public int getInt(String key){ return getRoot().getInt(key); }
    
    public String getString(String key){ return getRoot().getString(key); }
    
    public Boolean getBoolean(String key){ return getRoot().getBoolean(key); }
    
    public double getDouble(String key){ return getRoot().getDouble(key); }
}