package json.lexer;

import static junit.framework.TestCase.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import json.util.FileUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LexerTest {
    
    private JsonLexer lexer = new JsonLexer();
    private FileUtil fileUtil = new FileUtil();
    
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "/json/json-1.json", "/lexer/json-1-tokens.txt" },
            { "/json/json-2.json", "/lexer/json-2-tokens.txt" },
            { "/json/json-3.json", "/lexer/json-3-tokens.txt" },
            { "/json/json-4.json", "/lexer/json-4-tokens.txt" },
            { "/json/json-5.json", "/lexer/json-5-tokens.txt" },
            { "/json/json-6.json", "/lexer/json-6-tokens.txt" }
        });
    }
    
    private String input;
    private String expected;
    
    public LexerTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }
    
    @Test
    public void test() throws Exception {
        assertEquals(fileUtil.getTokens(expected), lexer.lex(fileUtil.getFileAsString(input)));
    }
    
}