package json.util;

/**
 * Utility class for extracting substrings from a stream of characters.
 */
public class SubstringExtractor {
    
    private int x = Integer.MAX_VALUE;
    private int y = Integer.MAX_VALUE;
    public void setX(int x) {
        if (x > this.x) return;
        this.x = x;
    }
    public void setY(int y) {
        if (y > this.y) return;
        this.y = y;
    }
    public void reset() {
        x = Integer.MAX_VALUE;
        y = Integer.MAX_VALUE;
    }
    public String getSubString(String s) {
        return s.substring(x,y);
    }
    @Override public String toString() {
        return "{x : " + x  + ", y: " + y + " }";
    }
}