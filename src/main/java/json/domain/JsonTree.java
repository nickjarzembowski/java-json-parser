package json.domain;

/**
 * Contains the root node of a json tree
 */
public class JsonTree {
    final JsonNode root;
    public JsonTree(final JsonNode root) {
        this.root = root;
    }
    public JsonNode getRoot() { return root.getChildren().get(0); }
    public JsonNode getNode(String key) { return getRoot().getNode(key); }
    public boolean containsKey(String key) { return getRoot().containsKey(key); }
    public boolean containsKeys(String... keys) { return getRoot().containsKeys(keys); }
    public int getInt(String key){ return getRoot().getInt(key); }
    public String getString(String key){ return getRoot().getString(key); }
    public Boolean getBoolean(String key){ return getRoot().getBoolean(key); }
    public double getDouble(String key){ return getRoot().getDouble(key); }
}