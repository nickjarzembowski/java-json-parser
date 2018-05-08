package json.lexer;

import java.util.ArrayList;
import java.util.List;

import json.lexer.domain.LexerState;
import json.lexer.domain.LexerToken;
import json.util.SubstringExtractorUtil;

public class JsonLexer {
    
    private String alphabet;
    private LexerState[][] table;
    
    public JsonLexer() {
        JsonLexerTable lexerTable = new JsonLexerTable();
        table = lexerTable.init();
        alphabet = lexerTable.alphabet;
    }
    
    public List<String> lex(String input) {
        return lex(input,false);
    }
    
    public List<String> lex(String input, boolean log) {
        
        SubstringExtractorUtil extractor = new SubstringExtractorUtil();
        List<String> lexed = new ArrayList<>();
        char currentChar;
        LexerState lastLexerState;
        LexerState currentLexerState = input.charAt(0) == '{' ? LexerState.OBJ_OPEN : LexerState.ELEMENTS;
        int index = 0;
        
        try {
            for (int i = 0; i < input.length(); i++) {
                index = i;
                currentChar = input.charAt(index);
                if (currentChar == ' ' || currentChar == '\n') continue;
                if (log) System.out.println(String.format("1: LexerState = %s | char = %s", currentLexerState, currentChar));
                
                int actionIdx = alphabet.indexOf(currentChar);
                int stateIdx = currentLexerState.ordinal();
                lastLexerState = currentLexerState;
                currentLexerState = table[stateIdx][actionIdx];
                
                String extracted = extractValue(extractor, i, currentLexerState, lastLexerState, input, lexed);
                if (!extracted.trim().isEmpty() && log) System.out.println(extracted);
                
                String extractedShape = extractShape(lastLexerState);
                if (!extractedShape.isEmpty()) {
                    lexed.add(extractedShape);
                    if (log) System.out.println(extractedShape);
                }
                if (doesStateTransitionRequireStepBack(currentLexerState, lastLexerState)) i--;
                if (currentLexerState.equals(LexerState.ERROR)) {
                    System.out.println(("Failed to parse json. Error at: " + input.substring(0, i) + " in " + input));
                }
            }
            if (currentLexerState.equals(LexerState.OBJ_PAIR_CLOSE) || currentLexerState.equals(LexerState.ARRAY_CLOSE)) {
                lexed.add(extractShape(currentLexerState).trim());
            } else {
                System.out.println("Invalid json character at index " + index + " in " + input + "");
            }
            if (log) System.out.println(currentLexerState.equals(LexerState.OBJ_PAIR_CLOSE));
        } catch (Exception e) {
            System.out.println("Lexing failed. " + e);
        }
        
        return lexed;
    }
    
    private String extractShape(LexerState currentLexerState) {
        if (currentLexerState.equals(LexerState.OBJ_OPEN)) {
            return LexerToken.OBJ_OPEN.name();
        } else if (currentLexerState.equals(LexerState.OBJ_PAIR_CLOSE)) {
            return LexerToken.OBJ_CLOSE.name();
        } else if (currentLexerState.equals(LexerState.ARRAY_OPEN)) {
            return LexerToken.ARR_OPEN.name();
        } else if (currentLexerState.equals(LexerState.ARRAY_CLOSE)) {
            return LexerToken.ARR_CLOSE.name();
        }
        return "";
    }
    
    private String extractValue(SubstringExtractorUtil extractor,
        int idx,
        LexerState currentLexerState,
        LexerState lastLexerState,
        String json,
        List<String> tokens
    ) {
        String result = "";
        if (currentLexerState.equals(LexerState.PAIR_STRING)) {
            extractor.setX(idx);
        }
        if (currentLexerState.equals(LexerState.PAIR_VALUE_1) && lastLexerState.equals(LexerState.PAIR_STRING)
            || currentLexerState.equals(LexerState.ARRAY_OPEN) && lastLexerState.equals(LexerState.ARRAY_VALUE)
            || currentLexerState.equals(LexerState.ELEMENTS) && lastLexerState.equals(LexerState.ARRAY_VALUE)
            || currentLexerState.equals(LexerState.ARRAY_CLOSE) && lastLexerState.equals(LexerState.ARRAY_VALUE)) {
            
            extractor.setY(idx);
            if (currentLexerState.equals(LexerState.ELEMENTS) && lastLexerState.equals(LexerState.ARRAY_VALUE)
                || currentLexerState.equals(LexerState.ARRAY_CLOSE) && lastLexerState.equals(LexerState.ARRAY_VALUE)) {
                
                result = LexerToken.ARR_VAL + extractor.getSubString(json).trim() + LexerToken.ARR_VAL_END;
                tokens.add(LexerToken.ARR_VAL.name());
                tokens.add( extractor.getSubString(json).trim().replace("\"",""));
                tokens.add(LexerToken.ARR_VAL_END.name());
            } else {
                result = LexerToken.KEY + " " + extractor.getSubString(json).trim();
                tokens.add(LexerToken.KEY.name());
                tokens.add( extractor.getSubString(json).trim());
            }
            extractor.reset();
        }
        if (currentLexerState.equals(LexerState.PAIR_VALUE_2)
            || currentLexerState.equals(LexerState.ARRAY_VALUE)
            || currentLexerState.equals(LexerState.PAIR_VALUE_3)) {
            extractor.setX(idx);
        }
        if (lastLexerState.equals(LexerState.PAIR_VALUE_2) && !currentLexerState.equals(LexerState.PAIR_VALUE_2)
            || currentLexerState.equals(LexerState.PAIR) && lastLexerState.equals(LexerState.ARRAY_VALUE)) {
            extractor.setY(idx);
            result = LexerToken.VAL + " " + extractor.getSubString(json).trim() + " " + LexerToken.VAL_END;
            tokens.add(LexerToken.VAL.name());
            tokens.add( extractor.getSubString(json).trim());
            tokens.add(LexerToken.VAL_END.name());
            extractor.reset();
        }
        
        return result + " ";
    }
    
    private boolean doesStateTransitionRequireStepBack(LexerState nextLexerState, LexerState lastLexerState) {
        return nextLexerState.equals(LexerState.MEMBERS) && lastLexerState.equals(LexerState.OBJ_PAIR_CLOSE)
            || nextLexerState.equals(LexerState.OBJ_OPEN) && lastLexerState.equals(LexerState.ELEMENTS)
            || nextLexerState.equals(LexerState.ARRAY_CLOSE) && lastLexerState.equals(LexerState.MEMBERS)
            || nextLexerState.equals(LexerState.PAIR) && lastLexerState.equals(LexerState.MEMBERS)
            || nextLexerState.equals(LexerState.PAIR) && lastLexerState.equals(LexerState.PAIR)
            || nextLexerState.equals(LexerState.VALUE) && lastLexerState.equals(LexerState.PAIR)
            || nextLexerState.equals(LexerState.PAIR_VALUE_1) && lastLexerState.equals(LexerState.VALUE)
            || nextLexerState.equals(LexerState.PAIR_STRING) && lastLexerState.equals(LexerState.VALUE)
            || nextLexerState.equals(LexerState.ARRAY_OPEN) && lastLexerState.equals(LexerState.PAIR_VALUE_2)
            || nextLexerState.equals(LexerState.ARRAY_VALUE) && lastLexerState.equals(LexerState.ARRAY_OPEN)
            || nextLexerState.equals(LexerState.OBJ_OPEN) && lastLexerState.equals(LexerState.ARRAY_OPEN)
            || nextLexerState.equals(LexerState.ELEMENTS) && lastLexerState.equals(LexerState.ARRAY_OPEN)
            || nextLexerState.equals(LexerState.OBJ_OPEN) && lastLexerState.equals(LexerState.PAIR_VALUE_1);
    }
}