package json.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class FileUtil {
    
    public String getFileAsString(String filename) {
        String str = "";
        try {
            str = IOUtils.toString(this.getClass().getResourceAsStream(filename), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
    
    public List<String> getTokens(String filename) {
        return Arrays.asList(getFileAsString(filename).split(", "));
    }
}
