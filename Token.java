public class Token {

    public enum Type {
        NUMBER,
        PLUS,
        MINUS,
        MULTIPLY,
        EQUAL,
        SMALLER_THAN,
        AND,
        DIVIDE,
        PARANTHESIS_OPEN,
        PARANTHESIS_CLOSE,
        EOF,
        PRINT,
        STRING,
        VAR,
        IDENTIFIER,
        BRACKET_OPEN,
        BRACKET_CLOSE,
        NUMBER_LITERAL,
        UNKNOWN,
        EQUALS,
        GREATER_THAN,
        IF,
        WHILE,
        END,
        READ,
        EOL,
    }

    private Type type;
    private String value;
    private int line;

    public Token(Type type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    public Type getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public int getLine() {
        return this.line;
    }
 }
