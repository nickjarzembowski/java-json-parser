package json.lexer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import json.util.SubstringExtractor;

public class JsonLexer {
    
    public enum LexerState {
        OBJ_OPEN,
        OBJ_PAIR_CLOSE,
        MEMBERS,
        PAIR,
        VALUE,
        PAIR_STRING,
        PAIR_VALUE_1,
        PAIR_VALUE_2,
        PAIR_VALUE_3,
        ARRAY_OPEN,
        ELEMENTS,
        ARRAY_CLOSE,
        ARRAY_VALUE,
        ERROR
    }
    
    public JsonLexer() {}
    
    static String lower = IntStream.rangeClosed('a', 'z').mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    static String upper = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    static String digits = IntStream.rangeClosed('0', '9').mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    static String chars = "{},:[]\" _-$./@+()\\!";
    static String alphabet = lower.concat(upper).concat(digits).concat(chars);
    static LexerState[][] table;
    
    /**
     * Builds the table which maps a Character and LexerState to a new LexerState.
     */
    private void init() {
        int totalStates = LexerState.values().length;
        int totalActions = alphabet.length();
        
        table = new LexerState[totalStates][totalActions];
        
        initTableToError(table,totalActions);
        
        updateCell(table, '{', LexerState.OBJ_OPEN, LexerState.MEMBERS, alphabet);
        updateCell(table, '}', LexerState.OBJ_OPEN, LexerState.OBJ_PAIR_CLOSE, alphabet);
        
        updateCell(table, '}', LexerState.OBJ_PAIR_CLOSE, LexerState.OBJ_PAIR_CLOSE, alphabet);
        updateCell(table, ',', LexerState.OBJ_PAIR_CLOSE, LexerState.MEMBERS, alphabet);
        updateCell(table, ']', LexerState.OBJ_PAIR_CLOSE, LexerState.ARRAY_CLOSE, alphabet);
        
        updateCell(table, '{', LexerState.MEMBERS, LexerState.OBJ_OPEN, alphabet);
        updateCell(table, ',', LexerState.MEMBERS, LexerState.ELEMENTS, alphabet);
        updateCell(table, '[', LexerState.MEMBERS, LexerState.ARRAY_OPEN, alphabet);
        updateCell(table, ']', LexerState.MEMBERS, LexerState.ARRAY_CLOSE, alphabet);
        fillAlphaNumeric(table, LexerState.MEMBERS, LexerState.PAIR);
        updateCell(table, '\"', LexerState.MEMBERS, LexerState.PAIR, alphabet);
        
        updateCell(table, ',', LexerState.PAIR, LexerState.PAIR, alphabet);
        fillAlphaNumeric(table, LexerState.PAIR, LexerState.VALUE);
        updateCell(table, '\"', LexerState.PAIR, LexerState.VALUE, alphabet);
        
        fillAlphaNumeric(table, LexerState.VALUE, LexerState.PAIR_VALUE_1);
        updateCell(table, '\"', LexerState.VALUE, LexerState.PAIR_STRING, alphabet);
        
        updateCell(table, ':', LexerState.PAIR_STRING, LexerState.PAIR_VALUE_1, alphabet);
        updateCell(table, '\"', LexerState.PAIR_STRING, LexerState.PAIR_STRING, alphabet);
        updateCell(table, '_', LexerState.PAIR_STRING, LexerState.PAIR_STRING, alphabet); //
        updateCell(table, ' ', LexerState.PAIR_STRING, LexerState.PAIR_STRING, alphabet); //
        fillAlphaNumeric(table, LexerState.PAIR_STRING, LexerState.PAIR_STRING);
        
        updateCell(table, '{', LexerState.PAIR_VALUE_1, LexerState.OBJ_OPEN, alphabet);
        updateCell(table, '[', LexerState.PAIR_VALUE_1, LexerState.ARRAY_OPEN, alphabet);
        updateCell(table, '\"', LexerState.PAIR_VALUE_1, LexerState.PAIR_VALUE_3, alphabet);
        updateCell(table, '-', LexerState.PAIR_VALUE_1, LexerState.PAIR_VALUE_2, alphabet);
        fillAlphaNumeric(table, LexerState.PAIR_VALUE_1, LexerState.PAIR_VALUE_2);
        
        updateCell(table, '}', LexerState.PAIR_VALUE_2, LexerState.OBJ_PAIR_CLOSE, alphabet);
        updateCell(table, ',', LexerState.PAIR_VALUE_2, LexerState.MEMBERS, alphabet);
        updateCell(table, '[', LexerState.PAIR_VALUE_2, LexerState.ARRAY_OPEN, alphabet);
        updateCell(table, ']', LexerState.PAIR_VALUE_2, LexerState.ARRAY_CLOSE, alphabet);
        updateBatchCells(table, "\"-$./:@+()\\!", LexerState.PAIR_VALUE_2, LexerState.PAIR_VALUE_2, alphabet);
        fillAlphaNumeric(table, LexerState.PAIR_VALUE_2, LexerState.PAIR_VALUE_2);
        
        updateCell(table, '\"', LexerState.PAIR_VALUE_3, LexerState.PAIR_VALUE_2, alphabet);
        fillAlphaNumeric(table, LexerState.PAIR_VALUE_3, LexerState.PAIR_VALUE_3);
        updateBatchCells(table, ",-$./:@+()\\!", LexerState.PAIR_VALUE_3, LexerState.PAIR_VALUE_3, alphabet);
        
        updateCell(table, '{', LexerState.ARRAY_OPEN, LexerState.OBJ_OPEN, alphabet);
        updateCell(table, '[', LexerState.ARRAY_OPEN, LexerState.ELEMENTS, alphabet);
        updateCell(table, ']', LexerState.ARRAY_OPEN, LexerState.ARRAY_CLOSE, alphabet);
        updateCell(table, '\"', LexerState.ARRAY_OPEN, LexerState.ELEMENTS, alphabet); //
        fillAlphaNumeric(table, LexerState.ARRAY_OPEN, LexerState.ARRAY_VALUE);
        
        updateCell(table, '[', LexerState.ELEMENTS, LexerState.ARRAY_OPEN, alphabet);
        updateCell(table, '{', LexerState.ELEMENTS, LexerState.OBJ_OPEN, alphabet);
        updateCell(table, '\"', LexerState.ELEMENTS, LexerState.ARRAY_VALUE, alphabet); //
        fillAlphaNumeric(table, LexerState.ELEMENTS, LexerState.ARRAY_VALUE);
        
        updateCell(table, '}', LexerState.ARRAY_CLOSE, LexerState.OBJ_PAIR_CLOSE, alphabet);
        updateCell(table, ',', LexerState.ARRAY_CLOSE, LexerState.PAIR, alphabet);
        updateCell(table, ']', LexerState.ARRAY_CLOSE, LexerState.ARRAY_CLOSE, alphabet);
        
        updateCell(table, ',', LexerState.ARRAY_VALUE, LexerState.ELEMENTS, alphabet);
        updateCell(table, ']', LexerState.ARRAY_VALUE, LexerState.ARRAY_CLOSE, alphabet);
        updateCell(table, '\"', LexerState.ARRAY_VALUE, LexerState.ARRAY_VALUE, alphabet);
        fillAlphaNumeric(table, LexerState.ARRAY_VALUE, LexerState.ARRAY_VALUE);
    }
    
    public List<String> lex(String input) throws Exception {
        return lex(input,false);
    }
    
    public List<String> lex(String input, boolean verbose) throws Exception {
        init();
        List<String> lexed = new ArrayList<>();
        SubstringExtractor extractor = new SubstringExtractor();
        char currentChar;
        LexerState lastLexerState;
        LexerState currentLexerState = input.charAt(0) == '{' ? LexerState.OBJ_OPEN : LexerState.ELEMENTS;
        int index = 0;
        try {
            for (int i = 0; i < input.length(); i++) {
                index = i;
                currentChar = input.charAt(index);
                if (currentChar == ' ' || currentChar == '\n') continue;
                if (verbose) System.out.println(String.format("1: LexerState = %s | char = %s", currentLexerState, currentChar));
                
                int actionIdx = alphabet.indexOf(currentChar);
                int stateIdx = currentLexerState.ordinal();
                lastLexerState = currentLexerState;
                currentLexerState = table[stateIdx][actionIdx];
                
                String extracted = extractValue(extractor, i, currentLexerState, lastLexerState, input, lexed);
                if (!extracted.trim().isEmpty() && verbose) System.out.println(extracted);
                
                String extractedShape = extractShape(lastLexerState);
                if (!extractedShape.isEmpty()) {
                    lexed.add(extractedShape);
                    if (verbose) System.out.println(extractedShape);
                }
                if (doesStateTransitionRequireStepBack(currentLexerState, lastLexerState)) i--;
                if (currentLexerState.equals(LexerState.ERROR)) {
                    throw new Exception("Failed to parse json. Error at: " + input.substring(0, i) + " in " + input);
                }
            }
            if (currentLexerState.equals(LexerState.OBJ_PAIR_CLOSE) || currentLexerState.equals(LexerState.ARRAY_CLOSE)) {
                lexed.add(extractShape(currentLexerState).trim());
            } else {
                throw new Exception("Invalid json character at index " + index + " in " + input + "");
            }
            if (verbose) System.out.println(currentLexerState.equals(LexerState.OBJ_PAIR_CLOSE));
            
            return lexed;
        } catch (Exception e) {
            throw new Exception("Lexing failed.", e);
        }
    }
    
    private String extractShape(LexerState currentLexerState) {
        String result = "";
        if (currentLexerState.equals(LexerState.OBJ_OPEN)) {
            result = "OBJ_OPEN";
        } else if (currentLexerState.equals(LexerState.OBJ_PAIR_CLOSE)) {
            result = "OBJ_CLOSE";
        } else if (currentLexerState.equals(LexerState.ARRAY_OPEN)) {
            result = "ARR_OPEN";
        } else if (currentLexerState.equals(LexerState.ARRAY_CLOSE)) {
            result = "ARR_CLOSE";
        }
        return result;
    }
    
    private String extractValue(SubstringExtractor extractor,
        int idx,
        LexerState currentLexerState,
        LexerState lastLexerState,
        String json,
        List<String> tokens) {
        String result = "";
        if (currentLexerState.equals(LexerState.PAIR_STRING)) {
            extractor.setX(idx);
        }
        if (currentLexerState.equals(LexerState.PAIR_VALUE_1) && lastLexerState.equals(LexerState.PAIR_STRING)
            || currentLexerState.equals(LexerState.ARRAY_OPEN) && lastLexerState.equals(LexerState.ARRAY_VALUE)
            || currentLexerState.equals(LexerState.ELEMENTS) && lastLexerState.equals(LexerState.ARRAY_VALUE)
            || currentLexerState.equals(LexerState.ARRAY_CLOSE) && lastLexerState.equals(LexerState.ARRAY_VALUE)
            ) {
            extractor.setY(idx);
            if (currentLexerState.equals(LexerState.ELEMENTS) && lastLexerState.equals(LexerState.ARRAY_VALUE)
                || currentLexerState.equals(LexerState.ARRAY_CLOSE) && lastLexerState.equals(LexerState.ARRAY_VALUE)
                ) {
                result = "ARR_VAL " + extractor.getSubString(json).trim() + " ARR_VAL_END";
                tokens.add("ARR_VAL");
                tokens.add( extractor.getSubString(json).trim().replace("\"",""));
                tokens.add("ARR_VAL_END");
            } else {
                result = "KEY " + extractor.getSubString(json).trim();
                tokens.add("KEY");
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
            result = "VAL " + extractor.getSubString(json).trim() + " VAL_END";
            tokens.add("VAL");
            tokens.add( extractor.getSubString(json).trim());
            tokens.add("VAL_END");
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
            || nextLexerState.equals(LexerState.OBJ_OPEN) && lastLexerState.equals(LexerState.PAIR_VALUE_1)
            ;
    }
    
    private void initTableToError(LexerState[][] table, int totalActions) {
        Stream.of(LexerState.values()).forEach(lexerState -> fillRow(table,totalActions, lexerState, LexerState.ERROR));
    }
    
    private void fillAlphaNumeric(LexerState[][] table, LexerState lexerState, LexerState newLexerState) {
        fillRange(table, alphabet.indexOf('a'), alphabet.indexOf('9'), lexerState, newLexerState);
    }
    
    private void updateBatchCells(LexerState[][] table, String str, LexerState lexerState, LexerState toLexerState, String alphabet) {
        IntStream.range(0, str.length()).forEach(i ->updateCell(table, str.charAt(i), lexerState, toLexerState, alphabet));
    }
    
    private void updateCell(LexerState[][] table, char c, LexerState lexerState, LexerState toLexerState, String alphabet) {
        int idx = alphabet.indexOf(c);
        table[lexerState.ordinal()][idx] = toLexerState;
    }
    
    private void fillRow(LexerState[][] table, int totalActions, LexerState lexerState, LexerState toLexerState) {
        IntStream.range(0, totalActions).forEach(i -> table[lexerState.ordinal()][i] = toLexerState);
    }
    
    private void fillRange(LexerState[][] table, int x, int y, LexerState lexerState, LexerState toLexerState) {
        IntStream.range(x, y+1).forEach(i -> table[lexerState.ordinal()][i] = toLexerState); // exclusive
    }
    
}
