package json.lexer.domain;

public enum  LexerState {
    OBJ_OPEN,
    OBJ_PAIR_CLOSE,
    MEMBERS,
    PAIR,
    VALUE,
    PAIR_STRING,
    PAIR_VALUE_1,
    PAIR_VALUE_2,
    PAIR_VALUE_3,
    ELEMENTS,
    ARRAY_OPEN,
    ARRAY_CLOSE,
    ARRAY_VALUE,
    ERROR
}
