package json.domain;

/**
 * Contains the root node of a json tree
 */
public class JsonTree {
    final JsonNode root;
    public JsonTree(final JsonNode root) {
        this.root = root;
    }
    public JsonNode getRoot() {
        return root.getChildren().get(0);
    }
}
