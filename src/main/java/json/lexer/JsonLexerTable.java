package json.lexer;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import json.lexer.domain.LexerState;

public class JsonLexerTable {
    
    private LexerState[][] table;
    
    public String lower = IntStream.rangeClosed('a', 'z').mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    public String upper = IntStream.rangeClosed('A', 'Z').mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    public String digits = IntStream.rangeClosed('0', '9').mapToObj(c -> "" + (char) c).collect(Collectors.joining());
    public String chars = "{},:[]\" _-$./@+()\\!";
    public String alphabet = lower.concat(upper).concat(digits).concat(chars);
    
    /**
     * Builds the table which maps a Character and LexerState to a new LexerState.
     */
    public LexerState[][] init() {
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
        updateBatchCells(table, "\"-$./@+()\\!", LexerState.PAIR_VALUE_2, LexerState.PAIR_VALUE_2, alphabet);
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
        
        return table;
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
        table[lexerState.ordinal()][alphabet.indexOf(c)] = toLexerState;
    }
    
    private void fillRow(LexerState[][] table, int totalActions, LexerState lexerState, LexerState toLexerState) {
        IntStream.range(0, totalActions).forEach(i -> table[lexerState.ordinal()][i] = toLexerState);
    }
    
    private void fillRange(LexerState[][] table, int x, int y, LexerState lexerState, LexerState toLexerState) {
        IntStream.range(x, y+1).forEach(i -> table[lexerState.ordinal()][i] = toLexerState); // exclusive
    }
    
}